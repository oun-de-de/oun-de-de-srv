package com.cdtphuhoi.oun_de_de.common.codelist;

import static com.cdtphuhoi.oun_de_de.common.Constants.DEFAULT_DESCRIPTION_FIELD_LENGTH;
import static com.cdtphuhoi.oun_de_de.common.Constants.DEFAULT_STRING_FIELD_LENGTH;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@Setter
public class CodeListRequest {

    @NotBlank
    @Size(max = DEFAULT_STRING_FIELD_LENGTH)
    private String name;

    @Size(max = DEFAULT_DESCRIPTION_FIELD_LENGTH)
    private String descr;
}
