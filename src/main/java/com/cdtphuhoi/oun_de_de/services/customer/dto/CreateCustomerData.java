package com.cdtphuhoi.oun_de_de.services.customer.dto;

import com.cdtphuhoi.oun_de_de.services.vehicle.dto.CreateVehicleData;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.validation.constraints.Min;

@Data
@Builder
public class CreateCustomerData {
    private LocalDateTime registerDate;

    private String code;

    private String name;

    private Boolean status;

    private Integer paymentTerm;

    /*
     * REFERER
     */
    private String referredById;

    // TODO: CodeList enhancement
    private String defaultPrice;

    // TODO: CodeList enhancement
    private String warehouseId;

    private String memo;

    // TODO: need to confirm
    private String profileUrl;

    // TODO: need to confirm
    private String shopBannerUrl;

    /*
     * USER
     */
    private String employeeId;


    /*
     * CONTACT
     */
    private String telephone;

    private String email;

    private String geography;

    private String address;

    private String location;

    private String map;

    private String billingAddress;

    private String deliveryAddress;

    /*
     * VEHICLES
     */
    private List<CreateVehicleData> vehicles;

}
