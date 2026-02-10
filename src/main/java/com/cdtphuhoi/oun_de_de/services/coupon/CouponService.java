package com.cdtphuhoi.oun_de_de.services.coupon;

import static com.cdtphuhoi.oun_de_de.utils.Utils.cambodiaNow;
import static com.cdtphuhoi.oun_de_de.utils.Utils.paddingZero;
import com.cdtphuhoi.oun_de_de.common.InvoiceStatus;
import com.cdtphuhoi.oun_de_de.common.InvoiceType;
import com.cdtphuhoi.oun_de_de.entities.Invoice;
import com.cdtphuhoi.oun_de_de.exceptions.ResourceNotFoundException;
import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import com.cdtphuhoi.oun_de_de.repositories.CouponRepository;
import com.cdtphuhoi.oun_de_de.repositories.InvoiceRepository;
import com.cdtphuhoi.oun_de_de.repositories.UserRepository;
import com.cdtphuhoi.oun_de_de.repositories.VehicleRepository;
import com.cdtphuhoi.oun_de_de.services.OrgManagementService;
import com.cdtphuhoi.oun_de_de.services.coupon.dto.CouponResult;
import com.cdtphuhoi.oun_de_de.services.coupon.dto.CreateCouponData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigInteger;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CouponService implements OrgManagementService {

    private final CouponRepository couponRepository;

    private final UserRepository userRepository;

    private final VehicleRepository vehicleRepository;

    private final InvoiceRepository invoiceRepository;

    public List<CouponResult> findAll() {
        return MapperHelpers.getCouponMapper().toListCouponResult(couponRepository.findAll());
    }

    public CouponResult create(CreateCouponData createCouponData) {
        var employee = userRepository.findOneById(createCouponData.getEmployeeId())
            .orElseThrow(
                () -> new ResourceNotFoundException(
                    String.format("Employee [id=%s] not found", createCouponData.getEmployeeId())
                )
            );
        var vehicle = vehicleRepository.findOneById(createCouponData.getVehicleId())
            .orElseThrow(
                () -> new ResourceNotFoundException(
                    String.format("Vehicle [id=%s] not found", createCouponData.getVehicleId())
                )
            );
        var coupon = MapperHelpers.getCouponMapper().toCoupon(createCouponData, employee, vehicle);
        var customer = vehicle.getCustomer();
        var invoice = Invoice.builder()
            .refNo(
                String.format(
                    "IN%s",
                    paddingZero(BigInteger.valueOf(invoiceRepository.count() + 1), 9)
                )
            )
            .customerName(customer.getName())
            .customer(customer)
            .date(cambodiaNow())
            .type(InvoiceType.INVOICE)
            .status(InvoiceStatus.OPEN)
            .coupon(coupon)
            .orgId(customer.getOrgId())
            .build();
        log.info("Creating coupon and invoice");
        var invoiceDb = invoiceRepository.save(invoice);
        log.info("Created coupon and invoice, id = {}", invoiceDb.getCoupon().getId());
        return MapperHelpers.getCouponMapper().toCouponResult(invoiceDb.getCoupon());
    }
}
