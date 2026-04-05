package com.cdtphuhoi.oun_de_de.controllers.dto.settings;

import com.cdtphuhoi.oun_de_de.common.UnitType;
import com.cdtphuhoi.oun_de_de.common.codelist.UpdateCodeListRequest;
import com.cdtphuhoi.oun_de_de.validators.ValueOfEnum;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateUnitRequest extends UpdateCodeListRequest {
    @ValueOfEnum(enumClass = UnitType.class)
    private String type;
}
