package com.cdtphuhoi.oun_de_de.services.settings.dto;

import com.cdtphuhoi.oun_de_de.common.UnitType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUnitData {
    private String name;

    private String descr;

    private UnitType type;
}
