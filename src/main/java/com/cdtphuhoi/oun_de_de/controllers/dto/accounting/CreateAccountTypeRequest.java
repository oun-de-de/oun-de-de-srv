package com.cdtphuhoi.oun_de_de.controllers.dto.accounting;

import static com.cdtphuhoi.oun_de_de.common.Constants.DEFAULT_CODE_LENGTH;
import com.cdtphuhoi.oun_de_de.common.codelist.CodeListRequest;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@Setter
public class CreateAccountTypeRequest extends CodeListRequest {

    @NotBlank
    @Size(max = DEFAULT_CODE_LENGTH)
    private String code;

    @NotBlank
    private String nature;
}
