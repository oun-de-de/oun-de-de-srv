package com.cdtphuhoi.oun_de_de.repositories;

import com.cdtphuhoi.oun_de_de.common.LoanInstallmentStatus;
import com.cdtphuhoi.oun_de_de.entities.LoanInstallment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import java.util.List;
import jakarta.persistence.LockModeType;

@Repository
public interface LoanInstallmentRepository extends JpaRepository<LoanInstallment, String>,
    JpaSpecificationExecutor<LoanInstallment> {

    List<LoanInstallment> findAllByLoanId(String loanId, Sort sort);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Page<LoanInstallment> findAll(Specification<LoanInstallment> specs, Pageable pageable);

    Long countByStatus(LoanInstallmentStatus loanInstallmentStatus);
}
