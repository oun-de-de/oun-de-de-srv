package com.cdtphuhoi.oun_de_de.services.payment;

import static com.cdtphuhoi.oun_de_de.utils.Utils.cambodiaNow;
import static com.cdtphuhoi.oun_de_de.utils.Utils.endOfDayInCambodia;
import static com.cdtphuhoi.oun_de_de.utils.Utils.startOfDayInCambodia;
import com.cdtphuhoi.oun_de_de.common.PaymentTermCycleStatus;
import com.cdtphuhoi.oun_de_de.entities.Customer;
import com.cdtphuhoi.oun_de_de.entities.Customer_;
import com.cdtphuhoi.oun_de_de.entities.PaymentTerm;
import com.cdtphuhoi.oun_de_de.entities.PaymentTermCycle;
import com.cdtphuhoi.oun_de_de.entities.PaymentTermCycle_;
import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import com.cdtphuhoi.oun_de_de.repositories.PaymentTermCycleRepository;
import com.cdtphuhoi.oun_de_de.repositories.PaymentTermRepository;
import com.cdtphuhoi.oun_de_de.services.OrgManagementService;
import com.cdtphuhoi.oun_de_de.services.payment.dto.PaymentTermCycleResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import jakarta.persistence.criteria.JoinType;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PaymentTermService implements OrgManagementService {

    private final PaymentTermCycleRepository paymentTermCycleRepository;

    private final PaymentTermRepository paymentTermRepository;

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
                PaymentTermCycleSpecifications.hasStatus(PaymentTermCycleStatus.OPEN)
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
        var cycle = PaymentTermCycle.builder()
            .customer(customer)
            .orgId(customer.getOrgId())
            .startDate(paymentTerm.getStartDate())
            .endDate(endOfDayInCambodia(paymentTerm.getStartDate().plusDays(paymentTerm.getDuration())))
            .status(PaymentTermCycleStatus.OPEN)
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
                PaymentTermCycleSpecifications.hasStatus(status),
                (root, query, cb) -> {
                    root.fetch(PaymentTermCycle_.CUSTOMER, JoinType.INNER);
                    return null;
                }
            ),
            pageable
        );
        return page.map(MapperHelpers.getPaymentMapper()::toPaymentTermCycleResult);
    }
}
