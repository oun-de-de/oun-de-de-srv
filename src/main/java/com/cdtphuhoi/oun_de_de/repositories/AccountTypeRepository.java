package com.cdtphuhoi.oun_de_de.repositories;

import com.cdtphuhoi.oun_de_de.entities.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountTypeRepository extends JpaRepository<AccountType, String>,
    BaseRepository<AccountType> {
}
