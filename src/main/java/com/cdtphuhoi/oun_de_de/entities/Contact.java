package com.cdtphuhoi.oun_de_de.entities;

import static com.cdtphuhoi.oun_de_de.common.DatabaseConstants.DEFAULT_DESCRIPTION_FIELD_LENGTH;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String telephone;

    private String email;

    // TODO: need to confirm
    private String geography;

    @Column(length = DEFAULT_DESCRIPTION_FIELD_LENGTH)
    private String address;

    // TODO: need to confirm
    private String location;

    // TODO: need to confirm
    private String map;

    @Column(length = DEFAULT_DESCRIPTION_FIELD_LENGTH)
    private String billingAddress;

    @Column(length = DEFAULT_DESCRIPTION_FIELD_LENGTH)
    private String deliveryAddress;
}
