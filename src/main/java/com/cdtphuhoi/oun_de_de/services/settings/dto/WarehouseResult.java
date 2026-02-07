package com.cdtphuhoi.oun_de_de.services.settings.dto;

import com.cdtphuhoi.oun_de_de.common.codelist.CodeListResult;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WarehouseResult extends CodeListResult {
    private String location;
}
