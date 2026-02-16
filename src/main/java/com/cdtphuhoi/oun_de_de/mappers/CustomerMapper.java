package com.cdtphuhoi.oun_de_de.mappers;

import com.cdtphuhoi.oun_de_de.controllers.dto.customer.CreateCustomerRequest;
import com.cdtphuhoi.oun_de_de.controllers.dto.customer.CreateProductSettingRequest;
import com.cdtphuhoi.oun_de_de.controllers.dto.customer.UpdateCustomerRequest;
import com.cdtphuhoi.oun_de_de.controllers.dto.customer.UpsertPaymentTermRequest;
import com.cdtphuhoi.oun_de_de.entities.Contact;
import com.cdtphuhoi.oun_de_de.entities.Customer;
import com.cdtphuhoi.oun_de_de.entities.PaymentTerm;
import com.cdtphuhoi.oun_de_de.entities.Product;
import com.cdtphuhoi.oun_de_de.entities.ProductSetting;
import com.cdtphuhoi.oun_de_de.entities.ProductSettingId;
import com.cdtphuhoi.oun_de_de.entities.User;
import com.cdtphuhoi.oun_de_de.services.customer.dto.ContactResult;
import com.cdtphuhoi.oun_de_de.services.customer.dto.CreateCustomerData;
import com.cdtphuhoi.oun_de_de.services.customer.dto.CreateProductSettingData;
import com.cdtphuhoi.oun_de_de.services.customer.dto.CustomerDetailsResult;
import com.cdtphuhoi.oun_de_de.services.customer.dto.CustomerResult;
import com.cdtphuhoi.oun_de_de.services.customer.dto.UpdateCustomerData;
import com.cdtphuhoi.oun_de_de.services.customer.dto.UpsertPaymentTermData;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(
    uses = {
        VehicleMapper.class,
        EmployeeMapper.class,
        SettingMapper.class
    },
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orgId", source = "employeeUser.orgId")
    Customer toCustomer(CreateCustomerData request, User employeeUser);

    @AfterMapping
    default void afterMapping(
        @MappingTarget Customer customer,
        CreateCustomerData request,
        User employeeUser
    ) {
        customer.setEmployee(employeeUser);
        customer.setContact(toContact(request, employeeUser));
        customer.setPaymentTerm(toPaymentTerm(request.getPaymentTerm(), customer, employeeUser));
        customer.setVehicles(
            MapperHelpers.getVehicleMapper().toListVehicles(request.getVehicles(), customer)
        );
    }

    void updateCustomerInternal(
        @MappingTarget Customer customer,
        UpdateCustomerData updateCustomerData
    );

    default void updateCustomer(
        @MappingTarget Customer customer,
        UpdateCustomerData updateCustomerData
    ) {
        updateCustomerInternal(customer, updateCustomerData);

        if (customer.getContact() == null) {
            return;
        }

        updateContact(customer.getContact(), updateCustomerData);
    }

    void updateContact(@MappingTarget Contact contact, UpdateCustomerData updateCustomerData);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orgId", source = "employeeUser.orgId")
    PaymentTerm toPaymentTerm(UpsertPaymentTermData request, Customer customer, User employeeUser);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orgId", source = "employeeUser.orgId")
    Contact toContact(CreateCustomerData request, User employeeUser);

    CreateCustomerData toCreateCustomerData(CreateCustomerRequest request);

    @Mapping(target = "referredBy", source = "customer.referredBy.name")
    CustomerResult toCustomerResult(Customer customer);

    UpdateCustomerData toUpdateCustomerData(UpdateCustomerRequest request);

    @Mapping(target = "customerReference.id", source = "customer.referredBy.id")
    @Mapping(target = "customerReference.name", source = "customer.referredBy.name")
    CustomerDetailsResult toCustomerDetailsResult(Customer customer);

    ContactResult toContactResult(Contact contact);

    CreateProductSettingData toCreateProductSettingData(CreateProductSettingRequest request);

    @Mapping(target = "orgId", source = "customer.orgId")
    @Mapping(target = "price", source = "createProductSettingData.price")
    @Mapping(target = "quantity", source = "createProductSettingData.quantity")
    @Mapping(target = "customer", source = "customer")
    @Mapping(target = "product", source = "product")
    ProductSetting toProductSetting(
        CreateProductSettingData createProductSettingData,
        Customer customer,
        Product product
    );

    @AfterMapping
    default void afterMapping(
        @MappingTarget ProductSetting productSetting,
        Customer customer,
        Product product
    ) {
        productSetting.setProductSettingId(
            ProductSettingId.builder()
                .customerId(customer.getId())
                .productId(product.getId())
                .build()
        );
    }

    UpsertPaymentTermData toUpsertPaymentTermData(UpsertPaymentTermRequest paymentTerm);
}
