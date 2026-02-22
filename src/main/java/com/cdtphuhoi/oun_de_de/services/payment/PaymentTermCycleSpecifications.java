package com.cdtphuhoi.oun_de_de.services.payment;

import com.cdtphuhoi.oun_de_de.common.PaymentTermCycleStatus;
import com.cdtphuhoi.oun_de_de.entities.Customer_;
import com.cdtphuhoi.oun_de_de.entities.PaymentTermCycle;
import com.cdtphuhoi.oun_de_de.entities.PaymentTermCycle_;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDateTime;
import java.util.Optional;

@UtilityClass
public class PaymentTermCycleSpecifications {

    public static Specification<PaymentTermCycle> hasCustomerId(String customerId) {
        return (root, query, cb) ->
            Optional.ofNullable(customerId)
                .map(
                    cusId -> cb.equal(
                        root.get(PaymentTermCycle_.CUSTOMER).get(Customer_.ID),
                        cusId
                    )
                )
                .orElse(null);
    }

    public static Specification<PaymentTermCycle> hasStartDateBetween(LocalDateTime from, LocalDateTime to) {
        return (root, query, cb) -> {
            var date = root.get(PaymentTermCycle_.startDate);
            if (from != null && to != null) {
                return cb.between(
                    date,
                    from,
                    to
                );
            }
            if (from != null) {
                return cb.greaterThanOrEqualTo(date, from);
            }
            if (to != null) {
                return cb.lessThanOrEqualTo(date, to);
            }
            return null;
        };
    }

    public static Specification<PaymentTermCycle> hasDuration(int duration) {
        return (root, query, cb) -> {
            var days =
                cb.sum(
                    cb.function(
                        "DATEDIFF",
                        Integer.class,
                        root.get(PaymentTermCycle_.END_DATE),
                        root.get(PaymentTermCycle_.START_DATE)
                    ),
                    cb.literal(1)
                );

            return cb.equal(days, duration);
        };


    }

    public static Specification<PaymentTermCycle> hasStatus(PaymentTermCycleStatus status) {
        return (root, query, cb) ->
            Optional.ofNullable(status)
                .map(
                    cycleStatus -> cb.equal(
                        root.get(PaymentTermCycle_.STATUS),
                        cycleStatus
                    )
                )
                .orElse(null);
    }
}
