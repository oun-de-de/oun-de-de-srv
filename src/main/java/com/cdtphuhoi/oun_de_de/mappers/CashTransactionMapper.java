package com.cdtphuhoi.oun_de_de.mappers;

import com.cdtphuhoi.oun_de_de.common.CashTransactionReason;
import com.cdtphuhoi.oun_de_de.common.CashTransactionType;
import com.cdtphuhoi.oun_de_de.controllers.dto.cash_transaction.CreateCashTransactionRequest;
import com.cdtphuhoi.oun_de_de.entities.CashTransaction;
import com.cdtphuhoi.oun_de_de.entities.CashTransactionDetail;
import com.cdtphuhoi.oun_de_de.entities.Currency;
import com.cdtphuhoi.oun_de_de.services.cash_transaction.dto.CashTransactionDetailResult;
import com.cdtphuhoi.oun_de_de.services.cash_transaction.dto.CashTransactionFlattenResult;
import com.cdtphuhoi.oun_de_de.services.cash_transaction.dto.CashTransactionResult;
import com.cdtphuhoi.oun_de_de.services.cash_transaction.dto.CreateCashTransactionData;
import com.cdtphuhoi.oun_de_de.utils.Utils;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ValueMapping;
import org.mapstruct.factory.Mappers;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mapper(
    imports = {
        Utils.class
    },
    uses = {
        AccountingMapper.class,
        SettingMapper.class,
        MapperHelpers.class,
    },
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    builder = @Builder(disableBuilder = true)
)
public interface CashTransactionMapper {
    CashTransactionMapper INSTANCE = Mappers.getMapper(CashTransactionMapper.class);

    @Mapping(target = "date", source = "request.date", defaultExpression = "java(Utils.cambodiaNow())")
    CreateCashTransactionData toCreateCashTransactionData(CreateCashTransactionRequest request);

    @ValueMapping(target = "DEBIT", source = "debit")
    @ValueMapping(target = "CREDIT", source = "credit")
    CashTransactionType stringToCashTransactionType(String source);

    @Mapping(target = "currency", source = "cashTransaction.currency.name")
    @Mapping(target = "employeeId", source = "cashTransaction.employee.id")
    CashTransactionResult toCashTransactionResult(CashTransaction cashTransaction);

    List<CashTransactionResult> toListCashTransactionResult(List<CashTransaction> result);

    @Mapping(target = "chartOfAccountId", source = "cashTransactionDetail.chartOfAccount.id")
    @Mapping(target = "accountTypeId", source = "cashTransactionDetail.accountType.id")
    @Mapping(target = "customerId", source = "cashTransactionDetail.customer.id")
    @Mapping(target = "journalClassId", source = "cashTransactionDetail.journalClass.id")
    CashTransactionDetailResult toCashTransactionDetailResult(CashTransactionDetail cashTransactionDetail);

    List<CashTransactionDetailResult> toListCashTransactionDetailResult(List<CashTransactionDetail> cashTransactionDetails);

    default List<CashTransactionFlattenResult> toSubListCashTransactionFlattenResult(
        CashTransaction cashTransaction
    ) {
        var result = new ArrayList<CashTransactionFlattenResult>();
        var defaultFlattenResult = getCashTransactionFlattenResult(cashTransaction);
        defaultFlattenResult.setReason(getDefaultReason(cashTransaction).toString());
        result.add(defaultFlattenResult);

        for (var cashTransactionDetail : cashTransaction.getCashTransactionDetails()) {
            var flattenResult = getCashTransactionFlattenResult(cashTransaction);
            flattenResult.setMemo(cashTransactionDetail.getMemo());
            flattenResult.setAmount(cashTransactionDetail.getAmount());
            if (cashTransactionDetail.getAccountType() != null) {
                flattenResult.setReason(cashTransactionDetail.getAccountType().getNature());
            } else {
                flattenResult.setReason(getDefaultReason(cashTransaction).toString());
            }
            result.add(flattenResult);
        }

        return result;
    }

    private static CashTransactionReason getDefaultReason(CashTransaction cashTransaction) {
        if (cashTransaction.getReason() != null) {
            return cashTransaction.getReason();
        }
        return CashTransactionType.DEBIT.equals(cashTransaction.getType()) ?
            CashTransactionReason.CASH_IN : CashTransactionReason.CASH_OUT;
    }

    private static CashTransactionFlattenResult getCashTransactionFlattenResult(CashTransaction cashTransaction) {
        var flattenResult = new CashTransactionFlattenResult();

        flattenResult.setId(cashTransaction.getId());
        flattenResult.setRefNo(cashTransaction.getRefNo());
        flattenResult.setType(cashTransaction.getType());
        flattenResult.setDate(cashTransaction.getDate());
        flattenResult.setMemo(cashTransaction.getMemo());
        flattenResult.setCurrency(
            Optional.ofNullable(cashTransaction.getCurrency())
                .map(Currency::getName)
                .orElse(null)
        );

        return flattenResult;
    }


    default List<CashTransactionFlattenResult> toListCashTransactionFlattenResults(
        List<CashTransaction> cashTransactions
    ) {
        if (cashTransactions.isEmpty()) {
            return List.of();
        }

        var result = new ArrayList<CashTransactionFlattenResult>();

        for (var cashTransaction : cashTransactions) {
            result.addAll(toSubListCashTransactionFlattenResult(cashTransaction));
        }

        return result;
    }
}
