package com.cdtphuhoi.oun_de_de.repositories;

import com.cdtphuhoi.oun_de_de.entities.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, String> {
    Page<Customer> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Optional<Customer> findOneById(String id);

}
