package com.cdtphuhoi.oun_de_de.services.coupon;

import static com.cdtphuhoi.oun_de_de.common.Constants.DEFAULT_PADDING_LENGTH;
import static com.cdtphuhoi.oun_de_de.utils.Utils.cambodiaNow;
import static com.cdtphuhoi.oun_de_de.utils.Utils.paddingZero;
import com.cdtphuhoi.oun_de_de.common.InvoiceType;
import com.cdtphuhoi.oun_de_de.entities.Customer;
import com.cdtphuhoi.oun_de_de.entities.Invoice;
import com.cdtphuhoi.oun_de_de.entities.WeightRecord;
import com.cdtphuhoi.oun_de_de.exceptions.ResourceNotFoundException;
import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import com.cdtphuhoi.oun_de_de.repositories.CouponRepository;
import com.cdtphuhoi.oun_de_de.repositories.InvoiceRepository;
import com.cdtphuhoi.oun_de_de.repositories.UserRepository;
import com.cdtphuhoi.oun_de_de.repositories.VehicleRepository;
import com.cdtphuhoi.oun_de_de.services.OrgManagementService;
import com.cdtphuhoi.oun_de_de.services.coupon.dto.CouponResult;
import com.cdtphuhoi.oun_de_de.services.coupon.dto.CreateCouponData;
import com.cdtphuhoi.oun_de_de.services.payment.PaymentTermService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CouponService implements OrgManagementService {

    private final static int DEFAULT_PAYMENT_TERM_DURATION = 1;

    private final CouponRepository couponRepository;

    private final UserRepository userRepository;

    private final VehicleRepository vehicleRepository;

    private final InvoiceRepository invoiceRepository;

    private final PaymentTermService paymentTermService;

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
        log.info("Creating coupon");
        var couponDb = couponRepository.save(coupon);
        log.info("Created coupon, id = {}", couponDb.getId());
        createInvoice(vehicle.getCustomer(), couponDb.getWeightRecords());
        return MapperHelpers.getCouponMapper().toCouponResult(couponDb);
    }

    private void createInvoice(Customer customer, List<WeightRecord> weightRecords) {
        var productWeightRecords = weightRecords.stream()
            .filter(
                weightRecord -> weightRecord.getProductName() != null &&
                    weightRecord.getPricePerProduct() != null &&
                    weightRecord.getQuantity() != null
            )
            .toList();
        var totalProductAmount = new AtomicReference<>(BigDecimal.ZERO);
        productWeightRecords.forEach(
            weightRecord -> {
                var amount = weightRecord.getPricePerProduct().multiply(weightRecord.getQuantity());
                weightRecord.setAmount(amount);
                totalProductAmount.updateAndGet(ca -> ca.add(amount));
            }
        );
        var cycle = Optional.ofNullable(paymentTermService.getActiveCurrentCycle(customer.getId()))
            .orElseGet(
                () -> paymentTermService.createNewCycle(customer, DEFAULT_PAYMENT_TERM_DURATION)
            );
        cycle.setTotalAmount(cycle.getTotalAmount().add(totalProductAmount.get()));
        var maxCurrentRefNo = Optional.ofNullable(invoiceRepository.findMaxRefNo(customer.getOrgId()))
            .orElse(0L);
        var invoice = Invoice.builder()
            .refNo(String.format("IN%s", paddingZero(BigInteger.valueOf(maxCurrentRefNo + 1), DEFAULT_PADDING_LENGTH)))
            .customerName(customer.getName())
            .customer(customer)
            .date(cambodiaNow())
            .type(InvoiceType.INVOICE)
            .orgId(customer.getOrgId())
            .weightRecords(
                productWeightRecords
            )
            .cycle(cycle)
            .build();
        productWeightRecords.forEach(weightRecord -> weightRecord.setInvoice(invoice));

        log.info("Creating invoice");
        var invoiceDb = invoiceRepository.save(invoice);
        log.info("Created invoice, id = {}", invoiceDb.getId());
    }
}
