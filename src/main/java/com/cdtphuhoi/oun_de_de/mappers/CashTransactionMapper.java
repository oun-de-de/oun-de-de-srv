package com.cdtphuhoi.oun_de_de.mappers;

import com.cdtphuhoi.oun_de_de.common.CashTransactionType;
import com.cdtphuhoi.oun_de_de.controllers.dto.cash_transaction.CreateCashTransactionRequest;
import com.cdtphuhoi.oun_de_de.entities.CashTransaction;
import com.cdtphuhoi.oun_de_de.entities.CashTransactionDetail;
import com.cdtphuhoi.oun_de_de.services.cash_transaction.dto.CashTransactionDetailResult;
import com.cdtphuhoi.oun_de_de.services.cash_transaction.dto.CashTransactionResult;
import com.cdtphuhoi.oun_de_de.services.cash_transaction.dto.CreateCashTransactionData;
import com.cdtphuhoi.oun_de_de.utils.Utils;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ValueMapping;
import org.mapstruct.factory.Mappers;
import java.util.List;

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

    @Mapping(target = "currencyId", source = "cashTransaction.currency.id")
    @Mapping(target = "employeeId", source = "cashTransaction.employee.id")
    CashTransactionResult toCashTransactionResult(CashTransaction cashTransaction);

    List<CashTransactionResult> toListCashTransactionResult(List<CashTransaction> result);

    @Mapping(target = "chartOfAccountId", source = "cashTransactionDetail.chartOfAccount.id")
    @Mapping(target = "accountTypeId", source = "cashTransactionDetail.accountType.id")
    @Mapping(target = "customerId", source = "cashTransactionDetail.customer.id")
    @Mapping(target = "journalClassId", source = "cashTransactionDetail.journalClass.id")
    CashTransactionDetailResult toCashTransactionDetailResult(CashTransactionDetail cashTransactionDetail);

    List<CashTransactionDetailResult> toListCashTransactionDetailResult(List<CashTransactionDetail> cashTransactionDetails);
}
