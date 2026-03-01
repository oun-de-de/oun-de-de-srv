package com.cdtphuhoi.oun_de_de.services.product.dto;

import com.cdtphuhoi.oun_de_de.services.settings.dto.UnitResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResult {
    private String id;

    private String name;

    private LocalDateTime date;

    private String refNo;

    private UnitResult unit;

    private DefaultProductSettingResult defaultProductSetting;
}
