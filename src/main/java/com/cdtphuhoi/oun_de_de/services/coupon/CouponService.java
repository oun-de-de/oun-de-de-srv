package com.cdtphuhoi.oun_de_de.services.coupon;

import static com.cdtphuhoi.oun_de_de.common.Constants.DEFAULT_PADDING_LENGTH;
import static com.cdtphuhoi.oun_de_de.utils.Utils.cambodiaNow;
import static com.cdtphuhoi.oun_de_de.utils.Utils.paddingZero;
import com.cdtphuhoi.oun_de_de.common.InvoiceType;
import com.cdtphuhoi.oun_de_de.entities.Coupon;
import com.cdtphuhoi.oun_de_de.entities.Coupon_;
import com.cdtphuhoi.oun_de_de.entities.Customer_;
import com.cdtphuhoi.oun_de_de.entities.Invoice;
import com.cdtphuhoi.oun_de_de.entities.Invoice_;
import com.cdtphuhoi.oun_de_de.entities.Vehicle_;
import com.cdtphuhoi.oun_de_de.entities.WeightRecord;
import com.cdtphuhoi.oun_de_de.exceptions.BadRequestException;
import com.cdtphuhoi.oun_de_de.exceptions.ResourceNotFoundException;
import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import com.cdtphuhoi.oun_de_de.repositories.CouponRepository;
import com.cdtphuhoi.oun_de_de.repositories.InvoiceRepository;
import com.cdtphuhoi.oun_de_de.repositories.PaymentTermCycleRepository;
import com.cdtphuhoi.oun_de_de.repositories.UserRepository;
import com.cdtphuhoi.oun_de_de.repositories.VehicleRepository;
import com.cdtphuhoi.oun_de_de.repositories.WeightRecordRepository;
import com.cdtphuhoi.oun_de_de.services.OrgManagementService;
import com.cdtphuhoi.oun_de_de.services.coupon.dto.CouponResult;
import com.cdtphuhoi.oun_de_de.services.coupon.dto.CreateCouponData;
import com.cdtphuhoi.oun_de_de.services.coupon.dto.UpdateCouponData;
import com.cdtphuhoi.oun_de_de.services.payment.PaymentTermService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import jakarta.persistence.criteria.JoinType;

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

    private final WeightRecordRepository weightRecordRepository;

    private final PaymentTermService paymentTermService;

    private final PaymentTermCycleRepository paymentTermCycleRepository;

    public Page<CouponResult> findAll(String customerId, Pageable pageable) {
        var page = couponRepository.findAll(
            Specification.allOf(
                (root, query, cb) ->
                    Optional.ofNullable(customerId)
                        .map(
                            cusId -> cb.equal(
                                root.get(Coupon_.VEHICLE).get(Vehicle_.CUSTOMER).get(Customer_.ID),
                                cusId
                            )
                        )
                        .orElse(null),
                (root, query, cb) -> {
                    if (query != null && Long.class != query.getResultType()) {
                        root.fetch(Coupon_.VEHICLE, JoinType.LEFT);
                        root.fetch(Coupon_.EMPLOYEE, JoinType.LEFT);
                        root.fetch(Coupon_.WEIGHT_RECORDS, JoinType.LEFT);
                    }
                    return null;
                }
            ),
            pageable
        );
        return page.map(MapperHelpers.getCouponMapper()::toCouponResult);
    }

    public CouponResult create(CreateCouponData createCouponData) {
        if (couponRepository.existsByCouponNo(createCouponData.getCouponNo())) {
            throw new BadRequestException(
                String.format("Coupon [code=%s] existed", createCouponData.getCouponNo())
            );
        }
        var employee = userRepository.findOneById(createCouponData.getEmployeeId())
            .orElseThrow(
                () -> new ResourceNotFoundException(
                    String.format("Employee [id=%s] not found", createCouponData.getEmployeeId())
                )
            );
        var vehicle = vehicleRepository.findOne(
                Specification.allOf(
                    (root, query, cb) -> {
                        if (query != null && Long.class != query.getResultType()) {
                            root.fetch(Vehicle_.CUSTOMER, JoinType.LEFT);
                        }
                        return cb.equal(
                            root.get(Vehicle_.ID),
                            createCouponData.getVehicleId()
                        );
                    }
                )
            )
            .orElseThrow(
                () -> new ResourceNotFoundException(
                    String.format("Vehicle [id=%s] not found", createCouponData.getVehicleId())
                )
            );
        var coupon = MapperHelpers.getCouponMapper().toCoupon(createCouponData, employee, vehicle);
        log.info("Creating coupon");
        var couponDb = couponRepository.save(coupon);
        log.info("Created coupon, id = {}", couponDb.getId());
        createInvoice(couponDb);
        return MapperHelpers.getCouponMapper().toCouponResult(couponDb);
    }

    private void createInvoice(Coupon coupon) {
        var customer = coupon.getVehicle().getCustomer();
        var weightRecords = coupon.getWeightRecords();
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
        coupon.setInvoiceRefNo(invoiceDb.getRefNo());
        couponRepository.save(coupon);
    }

    public CouponResult updateCouponByCouponNo(long couponNo, UpdateCouponData updateCouponData) {
        var coupon = couponRepository.findOne(
                Specification.allOf(
                    (root, query, cb) -> {
                        root.fetch(Coupon_.WEIGHT_RECORDS, JoinType.LEFT);
                        root.fetch(Coupon_.EMPLOYEE, JoinType.LEFT);
                        return cb.equal(
                            root.get(Coupon_.COUPON_NO),
                            couponNo
                        );
                    }
                )
            )
            .orElseThrow(
                () -> new ResourceNotFoundException(
                    String.format("Coupon [couponNo=%d] not found", couponNo)
                )
            );
        if (updateCouponData.getEmployeeId() != null &&
            !coupon.getEmployee().getId().equals(updateCouponData.getEmployeeId())) {
            var employee = userRepository.findOneById(updateCouponData.getEmployeeId())
                .orElseThrow(
                    () -> new ResourceNotFoundException(
                        String.format("Employee [id=%s] not found", updateCouponData.getEmployeeId())
                    )
                );
            coupon.setEmployee(employee);
        }
        // reset weight record
        var oldWeightRecords = coupon.getWeightRecords();
        MapperHelpers.getCouponMapper().updateCoupon(coupon, updateCouponData);
        // fill org_id
        coupon.setWeightRecords(
            MapperHelpers.getCouponMapper().toListWeightRecords(
                updateCouponData.getWeightRecords(),
                coupon.getEmployee(),
                coupon
            )
        );
        resetWeighRecordsAndInvoice(coupon.getInvoiceRefNo(), oldWeightRecords, coupon.getWeightRecords());
        var updated = couponRepository.save(coupon);
        return MapperHelpers.getCouponMapper().toCouponResult(updated);
    }

    private void resetWeighRecordsAndInvoice(
        String invoiceRefNo,
        List<WeightRecord> oldWeightRecords,
        List<WeightRecord> newWeightRecords) {
        var invoice = invoiceRepository.findOne(
                Specification.allOf(
                    (root, query, cb) -> {
                        root.fetch(Invoice_.WEIGHT_RECORDS, JoinType.LEFT);
                        root.fetch(Invoice_.CYCLE, JoinType.LEFT);
                        return cb.equal(
                            root.get(Invoice_.REF_NO),
                            invoiceRefNo
                        );
                    }
                )
            )
            .get();
        var productWeightRecords = newWeightRecords.stream()
            .filter(
                weightRecord -> weightRecord.getProductName() != null &&
                    weightRecord.getPricePerProduct() != null &&
                    weightRecord.getQuantity() != null
            )
            .toList();
        var totalAddedProductAmount = new AtomicReference<>(BigDecimal.ZERO);
        productWeightRecords.forEach(
            weightRecord -> {
                var amount = weightRecord.getPricePerProduct().multiply(weightRecord.getQuantity());
                weightRecord.setAmount(amount);
                totalAddedProductAmount.updateAndGet(ca -> ca.add(amount));
            }
        );
        var resetAmount = invoice.getWeightRecords().stream()
            .map(WeightRecord::getAmount)
            .reduce(
                BigDecimal.ZERO,
                BigDecimal::add
            );
        var cycle = invoice.getCycle();
        cycle.setTotalAmount(
            cycle.getTotalAmount()
                .subtract(resetAmount)
                .add(totalAddedProductAmount.get())
        );
        invoice.setWeightRecords(new ArrayList<>(productWeightRecords));
        productWeightRecords.forEach(weightRecord -> weightRecord.setInvoice(invoice));

        // remove oldRecord
        weightRecordRepository.deleteAllByIdIn(
            oldWeightRecords.stream()
                .map(WeightRecord::getId)
                .collect(Collectors.toSet())
        );
        invoiceRepository.save(invoice);
    }
}
