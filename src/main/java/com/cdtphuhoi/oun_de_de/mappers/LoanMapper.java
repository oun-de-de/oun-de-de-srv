package com.cdtphuhoi.oun_de_de.mappers;

import com.cdtphuhoi.oun_de_de.common.BorrowerType;
import com.cdtphuhoi.oun_de_de.controllers.dto.loan.CreateLoanRequest;
import com.cdtphuhoi.oun_de_de.entities.Loan;
import com.cdtphuhoi.oun_de_de.entities.LoanInstallment;
import com.cdtphuhoi.oun_de_de.services.loan.dto.CreateLoanData;
import com.cdtphuhoi.oun_de_de.services.loan.dto.LoanInstallmentResult;
import com.cdtphuhoi.oun_de_de.services.loan.dto.LoanResult;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ValueMapping;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    builder = @Builder(disableBuilder = true)
)
public interface LoanMapper {
    LoanMapper INSTANCE = Mappers.getMapper(LoanMapper.class);

    LoanResult toLoanResult(Loan loan);

    CreateLoanData toCreateLoanData(CreateLoanRequest request);

    @ValueMapping(target = "EMPLOYEE", source = "employee")
    @ValueMapping(target = "CUSTOMER", source = "customer")
    BorrowerType stringToBorrowerType(String source);

    @Mapping(target = "loanId", source = "loanInstallment.loan.id")
    LoanInstallmentResult toLoanInstallmentResult(LoanInstallment loanInstallment);

    List<LoanInstallmentResult> toListLoanInstallmentResult(List<LoanInstallment> installments);
}
