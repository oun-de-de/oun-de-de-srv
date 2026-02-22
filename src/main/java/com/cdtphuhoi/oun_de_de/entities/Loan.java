package com.cdtphuhoi.oun_de_de.entities;

import static com.cdtphuhoi.oun_de_de.common.Constants.DEFAULT_DECIMAL_PRECISION;
import static com.cdtphuhoi.oun_de_de.common.Constants.DEFAULT_DECIMAL_SCALE;
import static com.cdtphuhoi.oun_de_de.common.Constants.ORG_ID_COLUMN_NAME;
import static com.cdtphuhoi.oun_de_de.common.Constants.ORG_MANAGED_INDEX_NAME;
import static com.cdtphuhoi.oun_de_de.common.Constants.UUID_REFERENCE_LENGTH;
import com.cdtphuhoi.oun_de_de.common.BorrowerType;
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
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes = @Index(name = ORG_MANAGED_INDEX_NAME, columnList = ORG_ID_COLUMN_NAME))
public class Loan extends OrgManaged {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private BorrowerType borrowerType;

    @Column(nullable = false, length = UUID_REFERENCE_LENGTH)
    private String borrowerId;

    @Column(nullable = false, precision = DEFAULT_DECIMAL_PRECISION, scale = DEFAULT_DECIMAL_SCALE)
    private BigDecimal principalAmount;

    @Column(nullable = false)
    private Integer termMonths;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime createAt;
}
