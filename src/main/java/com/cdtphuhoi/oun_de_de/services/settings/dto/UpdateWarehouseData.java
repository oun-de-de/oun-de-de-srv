package com.cdtphuhoi.oun_de_de.services.settings.dto;

import com.cdtphuhoi.oun_de_de.common.codelist.CodeListData;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateWarehouseData extends CodeListData {
    private String location;
}
