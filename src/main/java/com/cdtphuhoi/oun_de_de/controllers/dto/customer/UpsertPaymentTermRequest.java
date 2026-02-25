package com.cdtphuhoi.oun_de_de.controllers.dto.customer;

import lombok.Data;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
public class UpsertPaymentTermRequest {

    @NotNull
    @Positive
    private Integer duration;

    @NotNull
    private LocalDateTime startDate;
}
