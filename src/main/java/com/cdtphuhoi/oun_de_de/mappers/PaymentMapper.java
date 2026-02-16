package com.cdtphuhoi.oun_de_de.mappers;

import com.cdtphuhoi.oun_de_de.controllers.dto.customer.UpsertPaymentTermRequest;
import com.cdtphuhoi.oun_de_de.entities.Customer;
import com.cdtphuhoi.oun_de_de.entities.PaymentTerm;
import com.cdtphuhoi.oun_de_de.services.customer.dto.PaymentTermResult;
import com.cdtphuhoi.oun_de_de.services.payment.dto.UpsertPaymentTermData;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    builder = @Builder(disableBuilder = true)
)
public interface PaymentMapper {
    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    PaymentTerm toPaymentTerm(UpsertPaymentTermData request);

    default PaymentTerm createOrgManagedPaymentTerm(UpsertPaymentTermData data, Customer customer) {
        var paymentTerm = toPaymentTerm(data);
        paymentTerm.setCustomer(customer);
        paymentTerm.setOrgId(customer.getOrgId());
        return paymentTerm;
    }

    void updatePaymentTerm(@MappingTarget PaymentTerm paymentTerm, UpsertPaymentTermData data);

    UpsertPaymentTermData toUpsertPaymentTermData(UpsertPaymentTermRequest paymentTerm);

    PaymentTermResult toPaymentTermResult(PaymentTerm paymentTerm);
}
