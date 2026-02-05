package com.cdtphuhoi.oun_de_de.controllers.dto.customer;

import static com.cdtphuhoi.oun_de_de.common.Constants.DEFAULT_DESCRIPTION_FIELD_LENGTH;
import static com.cdtphuhoi.oun_de_de.common.Constants.DEFAULT_STRING_FIELD_LENGTH;
import static com.cdtphuhoi.oun_de_de.common.Constants.DEFAULT_URL_FIELD_LENGTH;
import lombok.Data;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class CreateCustomerRequest {
    @NotNull
    private Date registerDate;

    @NotBlank
    @Size(max = DEFAULT_STRING_FIELD_LENGTH)
    private String code;

    @NotBlank
    @Size(max = DEFAULT_STRING_FIELD_LENGTH)
    private String name;

    @NotNull
    private Boolean status;

    /*
     * REFERER
     */
    private UUID referredById;

    // TODO: CodeList enhancement
    private String defaultPrice;

    // TODO: CodeList enhancement
    private String warehouse;

    @Size(max = DEFAULT_DESCRIPTION_FIELD_LENGTH)
    private String memo;

    // TODO: need to confirm
    @Size(max = DEFAULT_URL_FIELD_LENGTH)
    private String profileUrl;

    // TODO: need to confirm
    @Size(max = DEFAULT_URL_FIELD_LENGTH)
    private String shopBannerUrl;

    /*
     * USER
     */
    @NotNull
    private UUID employeeId;

    /*
     * CONTACT
     */
    @Size(max = DEFAULT_STRING_FIELD_LENGTH)
    private String telephone;

    @Size(max = DEFAULT_STRING_FIELD_LENGTH)
    private String email;

    // TODO: need to confirm
    private String geography;

    @Size(max = DEFAULT_DESCRIPTION_FIELD_LENGTH)
    private String address;

    // TODO: need to confirm
    private String location;

    // TODO: need to confirm
    private String map;

    @Size(max = DEFAULT_DESCRIPTION_FIELD_LENGTH)
    private String billingAddress;

    @Size(max = DEFAULT_DESCRIPTION_FIELD_LENGTH)
    private String deliveryAddress;

    /*
     * VEHICLES
     */
    @Valid
    private List<CreateVehicleRequest> vehicles;
}
