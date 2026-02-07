package com.cdtphuhoi.oun_de_de.controllers.dto.settings;

import com.cdtphuhoi.oun_de_de.common.codelist.CodeListRequest;
import com.cdtphuhoi.oun_de_de.common.UnitType;
import com.cdtphuhoi.oun_de_de.validators.ValueOfEnum;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class CreateUnitRequest extends CodeListRequest {

    @NotBlank
    @ValueOfEnum(enumClass = UnitType.class)
    private String type;
}
