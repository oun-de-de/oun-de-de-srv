package com.cdtphuhoi.oun_de_de.services.loan;

import com.cdtphuhoi.oun_de_de.common.BorrowerType;
import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import com.cdtphuhoi.oun_de_de.repositories.LoanRepository;
import com.cdtphuhoi.oun_de_de.services.OrgManagementService;
import com.cdtphuhoi.oun_de_de.services.loan.dto.LoanResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LoanService implements OrgManagementService {

    private final LoanRepository loanRepository;

    public Page<LoanResult> findBy(
        BorrowerType borrowerType,
        String borrowerId,
        LocalDateTime from,
        LocalDateTime to,
        Pageable pageable
    ) {
        var page = loanRepository.findAll(
            Specification.allOf(
                LoanSpecifications.hasBorrowerId(borrowerId),
                LoanSpecifications.hasStartDateBetween(from, to),
                LoanSpecifications.hasBorrowerType(borrowerType)
            ),
            pageable
        );
        return page.map(MapperHelpers.getLoanMapper()::toLoanResult);
    }
}
