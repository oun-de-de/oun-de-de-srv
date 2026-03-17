package com.cdtphuhoi.oun_de_de.services.accounting.dto;

import com.cdtphuhoi.oun_de_de.common.AccountNature;
import com.cdtphuhoi.oun_de_de.common.codelist.CodeListData;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountTypeResult extends CodeListData {

    private String code;

    private AccountNature nature;
}
