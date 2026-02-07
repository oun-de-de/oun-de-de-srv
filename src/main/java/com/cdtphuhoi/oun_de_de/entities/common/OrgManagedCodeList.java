package com.cdtphuhoi.oun_de_de.entities.common;

import static com.cdtphuhoi.oun_de_de.common.Constants.DEFAULT_DESCRIPTION_FIELD_LENGTH;
import static com.cdtphuhoi.oun_de_de.common.Constants.DEFAULT_STRING_FIELD_LENGTH;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class OrgManagedCodeList extends OrgManaged {
    @Column(length = DEFAULT_STRING_FIELD_LENGTH)
    private String name;

    @Column(length = DEFAULT_DESCRIPTION_FIELD_LENGTH)
    private String descr;
}
