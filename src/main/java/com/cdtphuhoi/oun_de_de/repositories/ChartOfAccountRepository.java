package com.cdtphuhoi.oun_de_de.repositories;

import com.cdtphuhoi.oun_de_de.entities.ChartOfAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ChartOfAccountRepository extends JpaRepository<ChartOfAccount, String>,
    JpaSpecificationExecutor<ChartOfAccount> {
}
