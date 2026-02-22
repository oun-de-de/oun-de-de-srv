package com.cdtphuhoi.oun_de_de.entities;

import static com.cdtphuhoi.oun_de_de.common.Constants.DEFAULT_DECIMAL_PRECISION;
import static com.cdtphuhoi.oun_de_de.common.Constants.DEFAULT_DECIMAL_SCALE;
import static com.cdtphuhoi.oun_de_de.common.Constants.ORG_ID_COLUMN_NAME;
import static com.cdtphuhoi.oun_de_de.common.Constants.ORG_MANAGED_INDEX_NAME;
import com.cdtphuhoi.oun_de_de.common.StockTransactionReason;
import com.cdtphuhoi.oun_de_de.common.StockTransactionType;
import com.cdtphuhoi.oun_de_de.entities.common.OrgManaged;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes = @Index(name = ORG_MANAGED_INDEX_NAME, columnList = ORG_ID_COLUMN_NAME))
public class StockTransaction extends OrgManaged {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
    private InventoryItem item;

    @Column(nullable = false, precision = DEFAULT_DECIMAL_PRECISION, scale = DEFAULT_DECIMAL_SCALE)
    private BigDecimal quantity;

    @Column(nullable = false)
    private StockTransactionType type;

    @Column(nullable = false)
    private StockTransactionReason reason;

    private String memo;

    /*
     * equipment borrow referenceId
     * One borrow = 1 EquipmentBorrow + 2 StockTransactions (out + in)
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.PERSIST)
    private EquipmentBorrow equipmentBorrow;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User createdBy;
}