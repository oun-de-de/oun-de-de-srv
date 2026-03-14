package com.cdtphuhoi.oun_de_de.repositories;

import com.cdtphuhoi.oun_de_de.entities.LoanPayment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LoanPaymentRepository extends JpaRepository<LoanPayment, String>,
    JpaSpecificationExecutor<LoanPayment> {
    List<LoanPayment> findAllByLoanId(String loanId, Sort by);
}
