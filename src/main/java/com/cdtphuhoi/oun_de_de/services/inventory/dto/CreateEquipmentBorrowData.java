package com.cdtphuhoi.oun_de_de.services.inventory.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CreateEquipmentBorrowData {
    private String customerId;

    private LocalDateTime expectedReturnDate;

    private BigDecimal quantity;

    private String memo;
}
