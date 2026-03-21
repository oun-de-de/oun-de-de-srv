package com.cdtphuhoi.oun_de_de.repositories;

import com.cdtphuhoi.oun_de_de.entities.CashTransactionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CashTransactionDetailRepository extends
    BaseRepository<CashTransactionDetail>,
    JpaRepository<CashTransactionDetail, String>,
    JpaSpecificationExecutor<CashTransactionDetail> {
}
