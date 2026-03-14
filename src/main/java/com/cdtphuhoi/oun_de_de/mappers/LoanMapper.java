package com.cdtphuhoi.oun_de_de.mappers;

import com.cdtphuhoi.oun_de_de.common.BorrowerType;
import com.cdtphuhoi.oun_de_de.controllers.dto.loan.CreateLoanPaymentRequest;
import com.cdtphuhoi.oun_de_de.controllers.dto.loan.CreateLoanRequest;
import com.cdtphuhoi.oun_de_de.controllers.dto.loan.ExtendLoanRequest;
import com.cdtphuhoi.oun_de_de.controllers.dto.loan.UpdateLoanRequest;
import com.cdtphuhoi.oun_de_de.entities.Loan;
import com.cdtphuhoi.oun_de_de.entities.LoanPayment;
import com.cdtphuhoi.oun_de_de.services.loan.dto.CreateLoanData;
import com.cdtphuhoi.oun_de_de.services.loan.dto.CreateLoanPaymentData;
import com.cdtphuhoi.oun_de_de.services.loan.dto.ExtendLoanData;
import com.cdtphuhoi.oun_de_de.services.loan.dto.LoanPaymentResult;
import com.cdtphuhoi.oun_de_de.services.loan.dto.LoanResult;
import com.cdtphuhoi.oun_de_de.services.loan.dto.UpdateLoanData;
import com.cdtphuhoi.oun_de_de.utils.Utils;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ValueMapping;
import org.mapstruct.factory.Mappers;
import java.util.List;
import jakarta.validation.Valid;

@Mapper(
    imports = {
        Utils.class
    },
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

    @Mapping(target = "paymentDate", source = "request.paymentDate", defaultExpression = "java(Utils.cambodiaNow())")
    CreateLoanPaymentData toCreateLoanPaymentData(CreateLoanPaymentRequest request);

    LoanPaymentResult toLoanPaymentResult(LoanPayment paymentDb);

    List<LoanPaymentResult> toListLoanPaymentResults(List<LoanPayment> payments);

    ExtendLoanData toExtendLoanData(ExtendLoanRequest request);

    UpdateLoanData toUpdateLoanData(@Valid UpdateLoanRequest request);

    void updateLoan(
        @MappingTarget Loan loan,
        UpdateLoanData updateLoanData
    );
}
