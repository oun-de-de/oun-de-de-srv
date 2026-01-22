package com.cdtphuhoi.oun_de_de.repository;

import com.cdtphuhoi.oun_de_de.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
