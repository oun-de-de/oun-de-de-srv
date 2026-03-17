package com.cdtphuhoi.oun_de_de.mappers;

import com.cdtphuhoi.oun_de_de.common.AccountNature;
import com.cdtphuhoi.oun_de_de.controllers.dto.accounting.CreateAccountTypeRequest;
import com.cdtphuhoi.oun_de_de.entities.AccountType;
import com.cdtphuhoi.oun_de_de.services.accounting.dto.AccountTypeResult;
import com.cdtphuhoi.oun_de_de.services.accounting.dto.CreateAccountTypeData;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ValueMapping;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(
    uses = MapperHelpers.class,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    builder = @Builder(disableBuilder = true)
)
public interface AccountingMapper {
    AccountingMapper INSTANCE = Mappers.getMapper(AccountingMapper.class);

    CreateAccountTypeData toCreateAccountTypeData(CreateAccountTypeRequest request);

    AccountType toAccountType(CreateAccountTypeData createAccountTypeData);

    AccountTypeResult toAccountTypeResult(AccountType accountTypeDb);

    List<AccountTypeResult> toListAccountTypeResults(List<AccountType> results);

    @ValueMapping(target = "ASSET", source = "asset")
    @ValueMapping(target = "LIABILITY", source = "liability")
    @ValueMapping(target = "EQUITY", source = "equity")
    @ValueMapping(target = "REVENUE", source = "revenue")
    @ValueMapping(target = "EXPENSE", source = "expense")
    AccountNature stringToAccountNature(String source);
}
