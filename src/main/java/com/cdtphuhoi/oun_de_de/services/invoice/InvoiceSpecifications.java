package com.cdtphuhoi.oun_de_de.services.invoice;

import com.cdtphuhoi.oun_de_de.common.InvoiceStatus;
import com.cdtphuhoi.oun_de_de.common.InvoiceType;
import com.cdtphuhoi.oun_de_de.entities.Customer_;
import com.cdtphuhoi.oun_de_de.entities.Invoice;
import com.cdtphuhoi.oun_de_de.entities.Invoice_;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDateTime;
import java.util.Optional;

@UtilityClass
public class InvoiceSpecifications {

    public static Specification<Invoice> hasCustomerId(String customerId) {
        return (root, query, cb) ->
            Optional.ofNullable(customerId)
                .map(
                    cId -> cb.equal(
                        root.get(Invoice_.CUSTOMER).get(Customer_.ID),
                        cId
                    )
                )
                .orElse(null);
    }

    public static Specification<Invoice> createBetween(LocalDateTime from, LocalDateTime to) {
        return (root, query, cb) -> {
            var date = root.get(Invoice_.date);
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

    public static Specification<Invoice> hasType(InvoiceType invoiceType) {
        return (root, query, cb) ->
            Optional.ofNullable(invoiceType)
                .map(
                    type -> cb.equal(
                        root.get(Invoice_.TYPE),
                        type
                    )
                )
                .orElse(null);
    }

    public static Specification<Invoice> hasStatus(InvoiceStatus invoiceStatus) {
        return (root, query, cb) ->
            Optional.ofNullable(invoiceStatus)
                .map(
                    status -> cb.equal(
                        root.get(Invoice_.STATUS),
                        status
                    )
                )
                .orElse(null);
    }
}
