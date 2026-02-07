package com.cdtphuhoi.oun_de_de.entities.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
@FilterDef(
    name = "orgFilter",
    parameters = @ParamDef(name = "orgId", type = String.class)
)
@Filter(
    name = "orgFilter",
    condition = "org_id = :orgId"
)
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class OrgManaged {
    @Column(name = "org_id", nullable = false, length = 20)
    private String orgId;
}
