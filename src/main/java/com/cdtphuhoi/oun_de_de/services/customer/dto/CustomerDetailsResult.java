package com.cdtphuhoi.oun_de_de.services.customer.dto;

import com.cdtphuhoi.oun_de_de.services.employee.dto.EmployeeResult;
import com.cdtphuhoi.oun_de_de.services.settings.dto.WarehouseResult;
import com.cdtphuhoi.oun_de_de.services.vehicle.dto.VehicleResult;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CustomerDetailsResult {
    private String id;

    private LocalDateTime registerDate;

    private String code;

    private String name;

    private Boolean status;

    private String memo;

    private String profileUrl;

    private String shopBannerUrl;

    private CustomerReference customerReference;

    private PaymentTermResult paymentTerm;

    private ContactResult contact;

    private EmployeeResult employee;

    private List<VehicleResult> vehicles;

    private WarehouseResult warehouse;
}
