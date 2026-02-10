package com.cdtphuhoi.oun_de_de.services.customer;

import com.cdtphuhoi.oun_de_de.entities.Customer;
import com.cdtphuhoi.oun_de_de.entities.Customer_;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import java.util.Optional;

@UtilityClass
public class CustomerSpecifications {

    public static Specification<Customer> containName(String nameOpt) {
        return (root, query, cb) ->
            Optional.ofNullable(nameOpt)
                .map(
                    name -> cb.like(
                        cb.lower(root.get(Customer_.NAME)),
                        "%" + name.toLowerCase() + "%"
                    )
                )
                .orElse(null);
    }

    public static Specification<Customer> hasPaymentTerm(Integer paymentTermOpt) {
        return (root, query, cb) ->
            Optional.ofNullable(paymentTermOpt)
                .map(
                    paymentTerm -> cb.equal(
                        root.get(Customer_.PAYMENT_TERM),
                        paymentTermOpt
                    )
                )
                .orElse(null);
    }
}
