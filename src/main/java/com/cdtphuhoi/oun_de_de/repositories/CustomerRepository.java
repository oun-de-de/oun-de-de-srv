package com.cdtphuhoi.oun_de_de.repositories;

import com.cdtphuhoi.oun_de_de.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String>,
    JpaSpecificationExecutor<Customer>, BaseRepository<Customer> {
}
