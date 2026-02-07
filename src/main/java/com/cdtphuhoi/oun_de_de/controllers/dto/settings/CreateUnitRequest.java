package com.cdtphuhoi.oun_de_de.controllers.dto.settings;

import com.cdtphuhoi.oun_de_de.common.CodeListRequest;
import com.cdtphuhoi.oun_de_de.common.UnitType;
import com.cdtphuhoi.oun_de_de.validators.ValueOfEnum;
import jakarta.validation.constraints.NotBlank;

public class CreateUnitRequest extends CodeListRequest {

    @NotBlank
    @ValueOfEnum(enumClass = UnitType.class)
    private String type;
}
