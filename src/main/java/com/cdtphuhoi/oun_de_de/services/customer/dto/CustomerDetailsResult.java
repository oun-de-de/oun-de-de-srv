package com.cdtphuhoi.oun_de_de.services.customer.dto;

import com.cdtphuhoi.oun_de_de.services.employee.dto.EmployeeResult;
import com.cdtphuhoi.oun_de_de.services.vehicle.dto.VehicleResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class CustomerDetailsResult {
    private String id;

    private Date registerDate;

    private String code;

    private String name;

    private Boolean status;

    // TODO: CodeList enhancement
    private String defaultPrice;

    // TODO: CodeList enhancement
    private String warehouse;

    private String memo;

    private String profileUrl;

    private String shopBannerUrl;

    private CustomerReference customerReference;

    private ContactResult contact;

    private EmployeeResult employee;

    private List<VehicleResult> vehicles;
}
