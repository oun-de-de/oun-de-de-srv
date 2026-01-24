package com.cdtphuhoi.oun_de_de.entities;

import static com.cdtphuhoi.oun_de_de.common.DatabaseConstants.DEFAULT_DESCRIPTION_FIELD_LENGTH;
import static com.cdtphuhoi.oun_de_de.common.DatabaseConstants.DEFAULT_URL_FIELD_LENGTH;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private Date registerDate;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean status;

    // TODO: CodeList enhancement
    @Column(nullable = false)
    private String customerType;

    // TODO: CodeList enhancement
    private String defaultPrice;

    // TODO: CodeList enhancement
    private String warehouse;

    @Column(length = DEFAULT_DESCRIPTION_FIELD_LENGTH)
    private String memo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User employee;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Contact contact;

    // TODO: need to confirm
    @Column(length = DEFAULT_URL_FIELD_LENGTH)
    private String profileUrl;

    // TODO: need to confirm
    @Column(length = DEFAULT_URL_FIELD_LENGTH)
    private String shopBannerUrl;
}
