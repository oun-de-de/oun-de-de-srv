package com.cdtphuhoi.oun_de_de.mappers;


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

    public static SettingMapper getSettingMapper() {
        return SettingMapper.INSTANCE;
    }

    public static InvoiceMapper getInvoiceMapper() {
        return InvoiceMapper.INSTANCE;
    }

    public static PaymentMapper getPaymentMapper() {
        return PaymentMapper.INSTANCE;
    }
}
