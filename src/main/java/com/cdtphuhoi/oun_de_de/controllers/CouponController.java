package com.cdtphuhoi.oun_de_de.controllers;

import static com.cdtphuhoi.oun_de_de.common.Constants.SWAGGER_SECURITY_SCHEME_NAME;
import com.cdtphuhoi.oun_de_de.services.coupon.dto.CreateCouponResult;
import com.cdtphuhoi.oun_de_de.controllers.dto.coupon.CreateCouponRequest;
import com.cdtphuhoi.oun_de_de.services.coupon.CouponService;
import com.cdtphuhoi.oun_de_de.utils.mappers.CouponMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = SWAGGER_SECURITY_SCHEME_NAME)
@RequestMapping("/api/v1/coupons")
public class CouponController {
    private final CouponService couponService;

    @PostMapping
    public ResponseEntity<CreateCouponResult> createCoupon(
        @Valid @RequestBody CreateCouponRequest request) {
        var response = couponService.create(CouponMapper.INSTANCE.toCreateCouponData(request));
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }
}
