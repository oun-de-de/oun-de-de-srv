package com.cdtphuhoi.oun_de_de.repositories;

import com.cdtphuhoi.oun_de_de.entities.PaymentTermCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PaymentTermCycleRepository extends JpaRepository<PaymentTermCycle, String>,
    JpaSpecificationExecutor<PaymentTermCycle> {

    Optional<PaymentTermCycle> findOneById(String id);
}
