package com.cdtphuhoi.oun_de_de.controllers.dto.customer;

import lombok.Data;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Data
public class UpsertPaymentTermRequest {

    @NotNull
    @PositiveOrZero
    private Integer duration;

    @NotNull
    private LocalDateTime startDate;
}
