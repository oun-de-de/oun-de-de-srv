package com.cdtphuhoi.oun_de_de.services.customer.dto;

import com.cdtphuhoi.oun_de_de.services.payment.dto.UpsertPaymentTermData;
import lombok.Data;

@Data
public class UpdateCustomerData {

    private String name;

    private Boolean status;

    /*
     * PAYMENT TERM
     */
    private UpsertPaymentTermData paymentTerm;

    /*
     * REFERER
     */
    private String referredById;

    // TODO: CodeList enhancement
    private String defaultPrice;

    /*
     * WAREHOUSE
     */
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
}
