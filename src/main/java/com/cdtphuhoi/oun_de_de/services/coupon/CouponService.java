package com.cdtphuhoi.oun_de_de.services.coupon;

import com.cdtphuhoi.oun_de_de.exceptions.ResourceNotFoundException;
import com.cdtphuhoi.oun_de_de.repositories.CouponRepository;
import com.cdtphuhoi.oun_de_de.repositories.UserRepository;
import com.cdtphuhoi.oun_de_de.repositories.VehicleRepository;
import com.cdtphuhoi.oun_de_de.services.OrgManagementService;
import com.cdtphuhoi.oun_de_de.services.coupon.dto.CouponResult;
import com.cdtphuhoi.oun_de_de.services.coupon.dto.CreateCouponData;
import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CouponService implements OrgManagementService {

    private final CouponRepository couponRepository;

    private final UserRepository userRepository;

    private final VehicleRepository vehicleRepository;

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
        return MapperHelpers.getCouponMapper().toCouponResult(couponDb);
    }
}
