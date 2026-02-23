package com.cdtphuhoi.oun_de_de.services.payment;

import static com.cdtphuhoi.oun_de_de.utils.Utils.cambodiaNow;
import static com.cdtphuhoi.oun_de_de.utils.Utils.endOfDayInCambodia;
import static com.cdtphuhoi.oun_de_de.utils.Utils.startOfDayInCambodia;
import com.cdtphuhoi.oun_de_de.common.PaymentTermCycleStatus;
import com.cdtphuhoi.oun_de_de.entities.Customer;
import com.cdtphuhoi.oun_de_de.entities.Customer_;
import com.cdtphuhoi.oun_de_de.entities.Payment;
import com.cdtphuhoi.oun_de_de.entities.PaymentTerm;
import com.cdtphuhoi.oun_de_de.entities.PaymentTermCycle;
import com.cdtphuhoi.oun_de_de.entities.PaymentTermCycle_;
import com.cdtphuhoi.oun_de_de.exceptions.BadRequestException;
import com.cdtphuhoi.oun_de_de.exceptions.ResourceNotFoundException;
import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import com.cdtphuhoi.oun_de_de.repositories.PaymentRepository;
import com.cdtphuhoi.oun_de_de.repositories.PaymentTermCycleRepository;
import com.cdtphuhoi.oun_de_de.repositories.PaymentTermRepository;
import com.cdtphuhoi.oun_de_de.services.OrgManagementService;
import com.cdtphuhoi.oun_de_de.services.invoice.dto.CreatePaymentData;
import com.cdtphuhoi.oun_de_de.services.invoice.dto.PaymentResult;
import com.cdtphuhoi.oun_de_de.services.payment.dto.PaymentTermCycleResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.criteria.JoinType;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PaymentTermService implements OrgManagementService {

    private final PaymentTermCycleRepository paymentTermCycleRepository;

    private final PaymentTermRepository paymentTermRepository;

    private final PaymentRepository paymentRepository;

    /*
     * @return active PaymentTermCycle, or else null
     */
    public PaymentTermCycle getActiveCurrentCycle(String customerId) {
        var activePaymentTermCycle = paymentTermCycleRepository.findOne(
            Specification.allOf(
                (root, query, cb) -> cb.equal(
                    root.get(PaymentTermCycle_.CUSTOMER).get(Customer_.ID),
                    customerId
                ),
                (root, query, cb) -> cb.greaterThanOrEqualTo(
                    root.get(PaymentTermCycle_.END_DATE),
                    cambodiaNow()
                ),
                PaymentTermCycleSpecifications.hasStatus(List.of(PaymentTermCycleStatus.OPEN))
            )
        );
        return activePaymentTermCycle.orElse(null);
    }

    public PaymentTermCycle createNewCycle(Customer customer, int defaultDuration) {
        var paymentTerm = paymentTermRepository.findOneByCustomerId(customer.getId())
            .orElseGet(
                // don't need to store in db
                () -> PaymentTerm.builder()
                    .duration(defaultDuration)
                    .startDate(startOfDayInCambodia(cambodiaNow()))
                    .build()
            );
        var now = cambodiaNow().toLocalDate();
        var startDate = paymentTerm.getStartDate().toLocalDate();
        var daysBetween = ChronoUnit.DAYS.between(startDate, now);
        var cycleIndex = Math.floorDiv(daysBetween, paymentTerm.getDuration());
        var cycleStart = paymentTerm.getStartDate().plusDays(cycleIndex * paymentTerm.getDuration());
        var cycleEnd = cycleStart.plusDays(paymentTerm.getDuration() - 1); // for 23:59:59
        var cycle = PaymentTermCycle.builder()
            .customer(customer)
            .orgId(customer.getOrgId())
            .startDate(cycleStart)
            .endDate(endOfDayInCambodia(cycleEnd))
            .status(PaymentTermCycleStatus.OPEN)
            .totalAmount(BigDecimal.ZERO)
            .totalPaidAmount(BigDecimal.ZERO)
            .build();
        return paymentTermCycleRepository.save(cycle);
    }

    public Page<PaymentTermCycleResult> findPaymentTermCyclesBy(
        String customerId,
        LocalDateTime from,
        LocalDateTime to,
        Integer duration,
        PaymentTermCycleStatus status,
        Pageable pageable
    ) {
        var page = paymentTermCycleRepository.findAll(
            Specification.allOf(
                PaymentTermCycleSpecifications.hasCustomerId(customerId),
                PaymentTermCycleSpecifications.hasStartDateBetween(from, to),
                PaymentTermCycleSpecifications.hasDuration(duration),
                PaymentTermCycleSpecifications.hasStatus(status == null ? List.of() : List.of(status)),
                (root, query, cb) -> {
                    root.fetch(PaymentTermCycle_.CUSTOMER, JoinType.INNER);
                    return null;
                }
            ),
            pageable
        );
        return page.map(MapperHelpers.getPaymentMapper()::toPaymentTermCycleResult);
    }

    public PaymentResult createPayment(String cycleId, CreatePaymentData createPaymentData) {
        var cycleOpt = paymentTermCycleRepository.findOne(
            Specification.allOf(
                (root, query, cb) -> cb.equal(root.get(PaymentTermCycle_.ID), cycleId),
                PaymentTermCycleSpecifications.hasStatus(List.of(PaymentTermCycleStatus.OPEN, PaymentTermCycleStatus.OVERDUE))
            )
        );
        if (cycleOpt.isEmpty()) {
            throw new ResourceNotFoundException(
                String.format("Cycle [id=%s] is not found or has already closed", cycleId)
            );
        }
        var cycle = updateCycle(createPaymentData, cycleOpt.get());
        var payment = Payment.builder()
            .orgId(cycle.getOrgId())
            .cycle(cycle)
            .paymentDate(createPaymentData.getPaymentDate())
            .amount(createPaymentData.getAmount())
            .build();
        log.info("Creating payment");
        var paymentDb = paymentRepository.save(payment);
        log.info("Created payment, id = {}", paymentDb.getId());
        return MapperHelpers.getPaymentMapper().toPaymentResult(paymentDb);
    }

    private static PaymentTermCycle updateCycle(CreatePaymentData createPaymentData, PaymentTermCycle cycle) {
        var remainingAmount = cycle.getTotalAmount().subtract(cycle.getTotalPaidAmount());
        if (createPaymentData.getAmount().compareTo(remainingAmount) > 0) {
            throw new BadRequestException(
                String.format("Not allowed to pay more than the remaining amount. " +
                    "Remaining amount: %.5f", remainingAmount)
            );
        }
        cycle.setTotalPaidAmount(cycle.getTotalPaidAmount().add(createPaymentData.getAmount()));
        return cycle;
    }
}
