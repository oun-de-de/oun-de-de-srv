package com.cdtphuhoi.oun_de_de.controllers.dto.coupon;

import com.cdtphuhoi.oun_de_de.controllers.dto.employee.EmployeeResponse;
import com.cdtphuhoi.oun_de_de.entities.Vehicle;
import com.cdtphuhoi.oun_de_de.entities.WeightRecord;
import lombok.Builder;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class CouponResponse {
    private String id;

    private Date date;

    private String driverName;

    private String remark;

    private Vehicle vehicle;

    private EmployeeResponse employee;

    private List<WeightRecord> weightRecords;
}
