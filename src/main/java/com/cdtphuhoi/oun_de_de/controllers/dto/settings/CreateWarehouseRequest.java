package com.cdtphuhoi.oun_de_de.controllers.dto.settings;

import static com.cdtphuhoi.oun_de_de.common.Constants.DEFAULT_DESCRIPTION_FIELD_LENGTH;
import static com.cdtphuhoi.oun_de_de.common.Constants.DEFAULT_STRING_FIELD_LENGTH;
import lombok.Data;
import jakarta.validation.constraints.Size;

@Data
public class CreateWarehouseRequest {
    @Size(max = DEFAULT_STRING_FIELD_LENGTH)
    private String name;

    @Size(max = DEFAULT_DESCRIPTION_FIELD_LENGTH)
    private String descr;
}
