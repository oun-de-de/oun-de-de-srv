package com.cdtphuhoi.oun_de_de.utils.mappers;


public class MapperHelpers {

    private MapperHelpers() {
    }

    public static CustomerMapper getCustomerMapper() {
        return CustomerMapper.INSTANCE;
    }

    public static CouponMapper getCouponMapper() {
        return CouponMapper.INSTANCE;
    }

    public static EmployeeMapper getEmployeeMapper() {
        return EmployeeMapper.INSTANCE;
    }

    public static VehicleMapper getVehicleMapper() {
        return VehicleMapper.INSTANCE;
    }

    public static ProductMapper getProductMapper() {
        return ProductMapper.INSTANCE;
    }

}
