package com.cdtphuhoi.oun_de_de.services.employee;

import com.cdtphuhoi.oun_de_de.controllers.dto.employee.EmployeeResponse;
import com.cdtphuhoi.oun_de_de.entities.User;
import com.cdtphuhoi.oun_de_de.exceptions.BadRequestException;
import com.cdtphuhoi.oun_de_de.exceptions.ConflictException;
import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import com.cdtphuhoi.oun_de_de.repositories.UserRepository;
import com.cdtphuhoi.oun_de_de.services.OrgManagementService;
import com.cdtphuhoi.oun_de_de.services.employee.dto.CreateEmployeeData;
import com.cdtphuhoi.oun_de_de.services.employee.dto.UpdateEmployeeProfileData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeService implements OrgManagementService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void createEmployee(CreateEmployeeData createEmployeeData) {
        if (!createEmployeeData.getPassword().equals(createEmployeeData.getReEnteredPassword())) {
            throw new BadRequestException("Passwords do not match");
        }
        if (userRepository.existsByUsername(createEmployeeData.getUsername())) {
            throw new ConflictException("Username already exists");
        }
        var createdByUser = userRepository.findOneById(createEmployeeData.getCreateById().toString())
            .orElseThrow(() -> new BadRequestException("Created by user does not exist"));

        var employee = User.builder()
            .username(createEmployeeData.getUsername())
            .password(passwordEncoder.encode(createEmployeeData.getPassword()))
            .orgId(createdByUser.getOrgId()) // must be same org
            .createdBy(createdByUser)
            .build();
        userRepository.save(employee);
    }

    public EmployeeResponse updateEmployeeProfile(String employeeId, UpdateEmployeeProfileData updateEmployeeData) {
        var employee = userRepository.findOneById(employeeId)
            .orElseThrow(
                () -> new BadRequestException(
                    String.format("Employee [id=%s] not found", employeeId)
                )
            );
        MapperHelpers.getEmployeeMapper().updateEmployee(employee, updateEmployeeData);
        var updated = userRepository.save(employee);
        return MapperHelpers.getEmployeeMapper().toEmployeeResponse(updated);
    }
}
