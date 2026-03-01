package com.cdtphuhoi.oun_de_de.controllers.dto.settings;

import static com.cdtphuhoi.oun_de_de.common.Constants.DEFAULT_STRING_FIELD_LENGTH;
import com.cdtphuhoi.oun_de_de.common.codelist.CodeListRequest;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Size;

@Getter
@Setter
public class UpdateWarehouseRequest extends CodeListRequest {
    @Size(max = DEFAULT_STRING_FIELD_LENGTH)
    private String location;
}
