package com.cdtphuhoi.oun_de_de.mappers;

import com.cdtphuhoi.oun_de_de.common.CashTransactionType;
import com.cdtphuhoi.oun_de_de.controllers.dto.cash_transaction.CreateCashTransactionRequest;
import com.cdtphuhoi.oun_de_de.services.cash_transaction.dto.CreateCashTransactionData;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ValueMapping;
import org.mapstruct.factory.Mappers;

@Mapper(
    uses = {
        AccountingMapper.class,
        MapperHelpers.class
    },
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    builder = @Builder(disableBuilder = true)
)
public interface CashTransactionMapper {
    CashTransactionMapper INSTANCE = Mappers.getMapper(CashTransactionMapper.class);

    CreateCashTransactionData toCreateCashTransactionData(CreateCashTransactionRequest request);

    @ValueMapping(target = "DEBIT", source = "debit")
    @ValueMapping(target = "CREDIT", source = "credit")
    CashTransactionType stringToCashTransactionType(String source);
}
