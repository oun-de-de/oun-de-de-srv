package com.cdtphuhoi.oun_de_de.controllers.dto.product;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
import jakarta.validation.constraints.DecimalMin;

@Data
public class CreateProductRequest {
    private String name;

    private Date date;

    private String refNo;

    @DecimalMin(value = "0.00", inclusive = true)
    private BigDecimal quantity;

    @DecimalMin(value = "0.00", inclusive = true)
    private BigDecimal cost;

    @DecimalMin(value = "0.00", inclusive = true)
    private BigDecimal price;
}
