package com.cdtphuhoi.oun_de_de.controllers.dto.invoice;

import static com.cdtphuhoi.oun_de_de.common.Constants.DEFAULT_CODE_LENGTH;
import static com.cdtphuhoi.oun_de_de.common.Constants.INV_CODE_ERROR_MSG;
import static com.cdtphuhoi.oun_de_de.common.Constants.INV_CODE_REGEX;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
public class CreatePaymentRequest {

    @NotBlank
    @Size(max = DEFAULT_CODE_LENGTH)
    @Pattern(regexp = INV_CODE_REGEX, message = INV_CODE_ERROR_MSG)
    private String code;

    private LocalDateTime paymentDate;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = false)
    private BigDecimal amount;
}
