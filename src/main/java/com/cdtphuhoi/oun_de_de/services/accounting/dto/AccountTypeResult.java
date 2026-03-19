package com.cdtphuhoi.oun_de_de.services.accounting.dto;

import com.cdtphuhoi.oun_de_de.common.codelist.CodeListResult;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountTypeResult extends CodeListResult {

    private String code;

    private String nature;
}
