package com.cdtphuhoi.oun_de_de.repositories;

import com.cdtphuhoi.oun_de_de.entities.CashTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CashTransactionRepository extends
    BaseRepository<CashTransaction>,
    JpaRepository<CashTransaction, String>,
    JpaSpecificationExecutor<CashTransaction> {
}
