package com.cdtphuhoi.oun_de_de.services.payment.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UpsertPaymentTermData {

    private Integer duration;

    private LocalDateTime startDate;
}
