package com.cdtphuhoi.oun_de_de.controllers.dto.inventory;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Data
public class CreateEquipmentBorrowRequest {
    /*
     * CUSTOMER
     */
    @NotNull
    private UUID customerId;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = false)
    private BigDecimal quantity;

    private LocalDateTime expectedReturnDate;

    private String memo;
}
