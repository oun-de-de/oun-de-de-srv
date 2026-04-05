package com.cdtphuhoi.oun_de_de.controllers.dto.settings;

import com.cdtphuhoi.oun_de_de.common.codelist.UpdateCodeListRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateSupplierRequest extends UpdateCodeListRequest {

    private String address;

    private String telephone;
}
