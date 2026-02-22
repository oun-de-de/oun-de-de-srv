package com.cdtphuhoi.oun_de_de.mappers;

import com.cdtphuhoi.oun_de_de.entities.Loan;
import com.cdtphuhoi.oun_de_de.services.loan.dto.LoanResult;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    builder = @Builder(disableBuilder = true)
)
public interface LoanMapper {
    LoanMapper INSTANCE = Mappers.getMapper(LoanMapper.class);

    LoanResult toLoanResult(Loan loan);
}
