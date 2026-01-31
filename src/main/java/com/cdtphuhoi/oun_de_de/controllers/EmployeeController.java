package com.cdtphuhoi.oun_de_de.controllers;

import static com.cdtphuhoi.oun_de_de.common.Constants.SWAGGER_SECURITY_SCHEME_NAME;
import com.cdtphuhoi.oun_de_de.controllers.dto.employee.CreateEmployeeRequest;
import com.cdtphuhoi.oun_de_de.controllers.dto.employee.EmployeeResponse;
import com.cdtphuhoi.oun_de_de.exceptions.ForbiddenException;
import com.cdtphuhoi.oun_de_de.services.auth.dto.UserDetailsImpl;
import com.cdtphuhoi.oun_de_de.services.employee.EmployeeService;
import com.cdtphuhoi.oun_de_de.services.employee.dto.CreateEmployeeData;
import com.cdtphuhoi.oun_de_de.utils.SecurityContextUtils;
import com.cdtphuhoi.oun_de_de.utils.Utils;
import com.cdtphuhoi.oun_de_de.utils.mappers.EmployeeMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;
import jakarta.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = SWAGGER_SECURITY_SCHEME_NAME)
@RequestMapping("/api/v1/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> listEmployees() {
        return ResponseEntity.ok(
            EmployeeMapper.INSTANCE.toListEmployeeResponse(employeeService.findAll())
        );
    }

    @PostMapping
    public ResponseEntity<String> createEmployee(
        @Valid @RequestBody CreateEmployeeRequest request) {
        employeeService.createEmployee(
            CreateEmployeeData.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .reEnteredPassword(request.getReEnteredPassword())
                .createById(
                    Optional.ofNullable(SecurityContextUtils.getCurrentUserProperty(UserDetailsImpl::getId))
                        .flatMap(Utils::toUUID)
                        .orElseThrow(() -> new ForbiddenException("Current user not found"))
                )
                .build()
        );
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body("Create employee successfully");
    }
}
