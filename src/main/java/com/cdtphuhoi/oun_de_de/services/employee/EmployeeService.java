package com.cdtphuhoi.oun_de_de.services.employee;

import com.cdtphuhoi.oun_de_de.entities.User;
import com.cdtphuhoi.oun_de_de.exceptions.BadRequestException;
import com.cdtphuhoi.oun_de_de.exceptions.ConflictException;
import com.cdtphuhoi.oun_de_de.repositories.UserRepository;
import com.cdtphuhoi.oun_de_de.services.employee.dto.CreateEmployeeData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createEmployee(CreateEmployeeData createEmployeeData) {
        if (!createEmployeeData.getPassword().equals(createEmployeeData.getReEnteredPassword())) {
            throw new BadRequestException("Passwords do not match");
        }
        if (userRepository.existsByUsername(createEmployeeData.getUsername())) {
            throw new ConflictException("Username already exists");
        }
        var createdByUser = userRepository.findById(createEmployeeData.getCreateById().toString())
            .orElseThrow(() -> new BadRequestException("Created by user does not exist"));

        var employee = User.builder()
            .username(createEmployeeData.getUsername())
            .password(passwordEncoder.encode(createEmployeeData.getPassword()))
            .build();
        // must be same org
        employee.setOrgId(createdByUser.getOrgId());
        userRepository.save(employee);
    }
}
