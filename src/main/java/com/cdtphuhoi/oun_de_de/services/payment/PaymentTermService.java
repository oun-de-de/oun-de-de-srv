package com.cdtphuhoi.oun_de_de.services.payment;

import static com.cdtphuhoi.oun_de_de.utils.Utils.cambodiaNow;
import static com.cdtphuhoi.oun_de_de.utils.Utils.endOfDayInCambodia;
import static com.cdtphuhoi.oun_de_de.utils.Utils.startOfDayInCambodia;
import com.cdtphuhoi.oun_de_de.entities.Customer;
import com.cdtphuhoi.oun_de_de.entities.Customer_;
import com.cdtphuhoi.oun_de_de.entities.PaymentTerm;
import com.cdtphuhoi.oun_de_de.entities.PaymentTermCycle;
import com.cdtphuhoi.oun_de_de.entities.PaymentTermCycle_;
import com.cdtphuhoi.oun_de_de.repositories.PaymentTermCycleRepository;
import com.cdtphuhoi.oun_de_de.repositories.PaymentTermRepository;
import com.cdtphuhoi.oun_de_de.services.OrgManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                )
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
            .build();
        return paymentTermCycleRepository.save(cycle);
    }
}
