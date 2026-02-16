package com.cdtphuhoi.oun_de_de.services.payment;

import static com.cdtphuhoi.oun_de_de.utils.Utils.cambodiaNow;
import com.cdtphuhoi.oun_de_de.entities.Customer_;
import com.cdtphuhoi.oun_de_de.entities.PaymentTermCycle;
import com.cdtphuhoi.oun_de_de.entities.PaymentTermCycle_;
import com.cdtphuhoi.oun_de_de.repositories.PaymentTermCycleRepository;
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
}
