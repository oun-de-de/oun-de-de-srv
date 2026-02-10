package com.cdtphuhoi.oun_de_de.entities;

import static com.cdtphuhoi.oun_de_de.common.Constants.ORG_ID_COLUMN_NAME;
import static com.cdtphuhoi.oun_de_de.common.Constants.ORG_MANAGED_INDEX_NAME;
import com.cdtphuhoi.oun_de_de.common.InvoiceStatus;
import com.cdtphuhoi.oun_de_de.common.InvoiceType;
import com.cdtphuhoi.oun_de_de.entities.common.OrgManaged;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes = @Index(name = ORG_MANAGED_INDEX_NAME, columnList = ORG_ID_COLUMN_NAME))
public class Invoice extends OrgManaged {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String refNo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Customer customer;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private InvoiceType type;

    @Column(nullable = false)
    private InvoiceStatus status;

    @OneToOne(
        cascade = CascadeType.PERSIST,
        fetch = FetchType.LAZY,
        optional = false
    )
    private Coupon coupon;
}
