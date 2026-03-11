package com.cdtphuhoi.oun_de_de.repositories;

import com.cdtphuhoi.oun_de_de.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {
    Optional<Vehicle> findOneById(String id);

    List<Vehicle> findAllByCustomerId(String customerId);

    Optional<Vehicle> findByIdAndCustomerId(String id, String customerId);

    @Query("SELECT v FROM Vehicle v JOIN FETCH v.customer WHERE v.id = :vehicleId")
    Optional<Vehicle> findByIdWithCustomer(@Param("vehicleId") String vehicleId);
}
