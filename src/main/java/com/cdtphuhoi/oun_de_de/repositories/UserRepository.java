package com.cdtphuhoi.oun_de_de.repositories;

import com.cdtphuhoi.oun_de_de.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String>, BaseRepository<User>,
    JpaSpecificationExecutor<User> {
    /**
     * direct fetching.
     * Filters apply to entity queries, but not to direct fetching.
     * @param id
     * @return
     */
    Optional<User> findById(String id);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}
