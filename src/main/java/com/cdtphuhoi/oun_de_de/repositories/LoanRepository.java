package com.cdtphuhoi.oun_de_de.repositories;

import com.cdtphuhoi.oun_de_de.entities.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, String>,
    JpaSpecificationExecutor<Loan> {

    Optional<Loan> findOneById(String id);
}
