package com.cdtphuhoi.oun_de_de.services.accounting;

import com.cdtphuhoi.oun_de_de.entities.AccountType_;
import com.cdtphuhoi.oun_de_de.entities.ChartOfAccount;
import com.cdtphuhoi.oun_de_de.entities.ChartOfAccount_;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import java.util.Optional;

@UtilityClass
public class ChartOfAccountSpecifications {

    public static Specification<ChartOfAccount> hasCode(String codeOpt) {
        return (root, query, cb) ->
            Optional.ofNullable(codeOpt)
                .map(
                    code -> cb.equal(
                        root.get(ChartOfAccount_.CODE),
                        code
                    )
                )
                .orElse(null);
    }

    public static Specification<ChartOfAccount> containName(String nameOpt) {
        return (root, query, cb) ->
            Optional.ofNullable(nameOpt)
                .map(
                    name -> cb.like(
                        cb.lower(root.get(ChartOfAccount_.NAME)),
                        "%" + name.toLowerCase() + "%"
                    )
                )
                .orElse(null);
    }

    public static Specification<ChartOfAccount> hasAccountTypeId(String accountTypeIdOpt) {
        return (root, query, cb) ->
            Optional.ofNullable(accountTypeIdOpt)
                .map(
                    accountTypeId -> cb.equal(
                        root.get(ChartOfAccount_.ACCOUNT_TYPE).get(AccountType_.ID),
                        accountTypeId
                    )
                )
                .orElse(null);
    }
}
