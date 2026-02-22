package com.cdtphuhoi.oun_de_de.entities;

import static com.cdtphuhoi.oun_de_de.common.Constants.DEFAULT_DECIMAL_PRECISION;
import static com.cdtphuhoi.oun_de_de.common.Constants.DEFAULT_DECIMAL_SCALE;
import static com.cdtphuhoi.oun_de_de.common.Constants.ORG_ID_COLUMN_NAME;
import static com.cdtphuhoi.oun_de_de.common.Constants.ORG_MANAGED_INDEX_NAME;
import com.cdtphuhoi.oun_de_de.common.ItemType;
import com.cdtphuhoi.oun_de_de.entities.common.OrgManaged;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes = @Index(name = ORG_MANAGED_INDEX_NAME, columnList = ORG_ID_COLUMN_NAME))
public class InventoryItem extends OrgManaged {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private ItemType type;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Unit unit;

    @Column(nullable = false, precision = DEFAULT_DECIMAL_PRECISION, scale = DEFAULT_DECIMAL_SCALE)
    private BigDecimal quantityOnHand;

    /*
     * Only applicable for CONSUMABLE
     * Nullable for equipment
     */
    @Column(precision = DEFAULT_DECIMAL_PRECISION, scale = DEFAULT_DECIMAL_SCALE)
    private BigDecimal alertThreshold;
}