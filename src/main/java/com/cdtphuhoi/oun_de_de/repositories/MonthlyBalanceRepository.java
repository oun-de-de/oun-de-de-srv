package com.cdtphuhoi.oun_de_de.repositories;

import com.cdtphuhoi.oun_de_de.entities.MonthlyBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MonthlyBalanceRepository extends JpaRepository<MonthlyBalance, String>,
    BaseRepository<MonthlyBalance> {

    Optional<MonthlyBalance> findByPeriod(String yearMonth);

    Optional<MonthlyBalance> findByPeriodAndOrgId(String previousMonth, String orgId);
}
