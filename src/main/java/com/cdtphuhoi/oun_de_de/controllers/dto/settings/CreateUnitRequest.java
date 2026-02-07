package com.cdtphuhoi.oun_de_de.controllers.dto.settings;

import static com.cdtphuhoi.oun_de_de.common.Constants.DEFAULT_DESCRIPTION_FIELD_LENGTH;
import static com.cdtphuhoi.oun_de_de.common.Constants.DEFAULT_STRING_FIELD_LENGTH;
import com.cdtphuhoi.oun_de_de.common.UnitType;
import com.cdtphuhoi.oun_de_de.validators.ValueOfEnum;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class CreateUnitRequest {

    @Size(max = DEFAULT_STRING_FIELD_LENGTH)
    private String name;

    @Size(max = DEFAULT_DESCRIPTION_FIELD_LENGTH)
    private String descr;

    @NotBlank
    @ValueOfEnum(enumClass = UnitType.class)
    private String type;
}
