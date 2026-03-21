package com.cdtphuhoi.oun_de_de.repositories;

import com.cdtphuhoi.oun_de_de.entities.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface AccountTypeRepository extends JpaRepository<AccountType, String>,
    BaseRepository<AccountType> {

    List<AccountType> findAllByIdIn(Collection<String> ids);
}
