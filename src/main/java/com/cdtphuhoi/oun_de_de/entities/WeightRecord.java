package com.cdtphuhoi.oun_de_de.entities;

import static com.cdtphuhoi.oun_de_de.common.Constants.DEFAULT_DECIMAL_PRECISION;
import static com.cdtphuhoi.oun_de_de.common.Constants.DEFAULT_DECIMAL_SCALE;
import static com.cdtphuhoi.oun_de_de.common.Constants.ORG_ID_COLUMN_NAME;
import static com.cdtphuhoi.oun_de_de.common.Constants.ORG_MANAGED_INDEX_NAME;
import com.cdtphuhoi.oun_de_de.entities.common.OrgManaged;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes = @Index(name = ORG_MANAGED_INDEX_NAME, columnList = ORG_ID_COLUMN_NAME))
public class WeightRecord extends OrgManaged {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String productName;

    private String unit;

    @Column(precision = DEFAULT_DECIMAL_PRECISION, scale = DEFAULT_DECIMAL_SCALE)
    private BigDecimal pricePerProduct;

    @Column(precision = DEFAULT_DECIMAL_PRECISION, scale = DEFAULT_DECIMAL_SCALE)
    private BigDecimal quantityPerProduct;

    @Column(precision = DEFAULT_DECIMAL_PRECISION, scale = DEFAULT_DECIMAL_SCALE)
    private BigDecimal quantity;

    @Column(precision = DEFAULT_DECIMAL_PRECISION, scale = DEFAULT_DECIMAL_SCALE)
    private BigDecimal weight;

    private LocalDateTime outTime;

    private boolean isManual;

    private String memo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Invoice invoice;
}
