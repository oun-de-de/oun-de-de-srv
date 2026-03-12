package com.cdtphuhoi.oun_de_de.repositories;

import com.cdtphuhoi.oun_de_de.entities.ProductSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;
import java.util.List;

public interface ProductSettingRepository extends JpaRepository<ProductSetting, String> {
    List<ProductSetting> findAllByCustomerId(String customerId);

    List<ProductSetting> findAllByCustomerIdIn(Collection<String> customerIds);
}
