package com.cdtphuhoi.oun_de_de.services.inventory.dto;

import com.cdtphuhoi.oun_de_de.common.BorrowStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class EquipmentBorrowResult {
    private String id;

    private String itemId;

    private String customerId;

    private BigDecimal quantity;

    private LocalDateTime borrowDate;

    private LocalDateTime expectedReturnDate;

    private LocalDateTime actualReturnDate;

    private BorrowStatus status;
}
