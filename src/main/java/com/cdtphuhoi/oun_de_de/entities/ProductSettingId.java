package com.cdtphuhoi.oun_de_de.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ProductSettingId {
    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "product_id")
    private String productId;
}
