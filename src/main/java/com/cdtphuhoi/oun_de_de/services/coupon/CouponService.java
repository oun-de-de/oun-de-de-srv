package com.cdtphuhoi.oun_de_de.services.coupon;

import com.cdtphuhoi.oun_de_de.exceptions.ResourceNotFoundException;
import com.cdtphuhoi.oun_de_de.repositories.CouponRepository;
import com.cdtphuhoi.oun_de_de.repositories.UserRepository;
import com.cdtphuhoi.oun_de_de.repositories.VehicleRepository;
import com.cdtphuhoi.oun_de_de.services.OrgManagementService;
import com.cdtphuhoi.oun_de_de.services.coupon.dto.CreateCouponResult;
import com.cdtphuhoi.oun_de_de.services.coupon.dto.CreateCouponData;
import com.cdtphuhoi.oun_de_de.utils.mappers.CouponMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponService implements OrgManagementService {

    private final CouponRepository couponRepository;

    private final UserRepository userRepository;

    private final VehicleRepository vehicleRepository;

    public CreateCouponResult create(CreateCouponData createCouponData) {
        var employee = userRepository.findById(createCouponData.getEmployeeId())
            .orElseThrow(
                () -> new ResourceNotFoundException(
                    String.format("Employee [id=%s] not found", createCouponData.getEmployeeId())
                )
            );
        var vehicle = vehicleRepository.findById(createCouponData.getVehicleId())
            .orElseThrow(
                () -> new ResourceNotFoundException(
                    String.format("Vehicle [id=%s] not found", createCouponData.getVehicleId())
                )
            );
        var coupon = CouponMapper.INSTANCE.toCoupon(createCouponData, employee, vehicle);
        log.info("Creating coupon");
        var couponDb = couponRepository.save(coupon);
        log.info("Created coupon, id = {}", couponDb.getId());
        return CouponMapper.INSTANCE.toCouponResult(couponDb);
    }
}
