package com.cdtphuhoi.oun_de_de.controllers.dto.settings;

import com.cdtphuhoi.oun_de_de.common.codelist.CodeListRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSupplierRequest extends CodeListRequest {

    private String address;

    private String telephone;
}
