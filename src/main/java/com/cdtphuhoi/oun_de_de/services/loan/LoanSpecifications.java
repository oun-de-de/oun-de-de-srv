package com.cdtphuhoi.oun_de_de.services.loan;

import com.cdtphuhoi.oun_de_de.common.BorrowerType;
import com.cdtphuhoi.oun_de_de.entities.Loan;
import com.cdtphuhoi.oun_de_de.entities.Loan_;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDateTime;
import java.util.Optional;

@UtilityClass
public class LoanSpecifications {

    public static Specification<Loan> hasStartDateBetween(LocalDateTime from, LocalDateTime to) {
        return (root, query, cb) -> {
            var date = root.get(Loan_.startDate);
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

    public static Specification<Loan> hasBorrowerId(String borrowerId) {
        return (root, query, cb) ->
            Optional.ofNullable(borrowerId)
                .map(
                    brrwrId -> cb.equal(
                        root.get(Loan_.BORROWER_ID),
                        brrwrId
                    )
                )
                .orElse(null);
    }

    public static Specification<Loan> hasBorrowerType(BorrowerType borrowerType) {
        return (root, query, cb) ->
            Optional.ofNullable(borrowerType)
                .map(
                    type -> cb.equal(
                        root.get(Loan_.BORROWER_TYPE),
                        type
                    )
                )
                .orElse(null);
    }
}
