package com.cdtphuhoi.oun_de_de.controllers.dto.invoice;

import lombok.Data;
import java.time.LocalDateTime;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Data
public class ConvertToLoanRequest {

    @NotNull
    @Min(value = 1)
    private Integer termMonths;

    @NotNull
    private LocalDateTime startDate;
}
