package com.cdtphuhoi.oun_de_de.mappers;

import com.cdtphuhoi.oun_de_de.common.AccountNature;
import com.cdtphuhoi.oun_de_de.controllers.dto.accounting.CreateAccountTypeRequest;
import com.cdtphuhoi.oun_de_de.controllers.dto.accounting.CreateChartOfAccountRequest;
import com.cdtphuhoi.oun_de_de.controllers.dto.accounting.CreateJournalClassRequest;
import com.cdtphuhoi.oun_de_de.controllers.dto.accounting.CreateJournalTypeRequest;
import com.cdtphuhoi.oun_de_de.entities.AccountType;
import com.cdtphuhoi.oun_de_de.entities.ChartOfAccount;
import com.cdtphuhoi.oun_de_de.entities.JournalClass;
import com.cdtphuhoi.oun_de_de.entities.JournalType;
import com.cdtphuhoi.oun_de_de.services.accounting.dto.AccountTypeResult;
import com.cdtphuhoi.oun_de_de.services.accounting.dto.ChartOfAccountResult;
import com.cdtphuhoi.oun_de_de.services.accounting.dto.CreateAccountTypeData;
import com.cdtphuhoi.oun_de_de.services.accounting.dto.CreateChartOfAccountData;
import com.cdtphuhoi.oun_de_de.services.accounting.dto.CreateJournalClassData;
import com.cdtphuhoi.oun_de_de.services.accounting.dto.CreateJournalTypeData;
import com.cdtphuhoi.oun_de_de.services.accounting.dto.JournalClassResult;
import com.cdtphuhoi.oun_de_de.services.accounting.dto.JournalTypeResult;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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

    JournalClassResult toJournalClassResult(JournalClass journalClass);

    List<JournalClassResult> toListJournalClassResults(List<JournalClass> results);

    JournalClass toJournalClass(CreateJournalClassData createJournalClassData);

    CreateJournalClassData toCreateJournalClassData(CreateJournalClassRequest request);

    JournalTypeResult toJournalTypeResult(JournalType journalType);

    List<JournalTypeResult> toListJournalTypeResults(List<JournalType> results);

    CreateJournalTypeData toCreateJournalTypeData(CreateJournalTypeRequest request);

    JournalType toJournalType(CreateJournalTypeData createJournalTypeData);

    CreateChartOfAccountData toCreateChartOfAccountData(CreateChartOfAccountRequest request);

    ChartOfAccount toChartOfAccount(CreateChartOfAccountData createChartOfAccountData);

    @Mapping(target = "accountType.id", source = "accountType.id")
    ChartOfAccountResult toChartOfAccountResult(ChartOfAccount chartOfAccountDb);
}
