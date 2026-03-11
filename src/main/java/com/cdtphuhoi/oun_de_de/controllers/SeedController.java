package com.cdtphuhoi.oun_de_de.controllers;

import static com.cdtphuhoi.oun_de_de.common.Constants.DEFAULT_PADDING_LENGTH;
import static com.cdtphuhoi.oun_de_de.common.Constants.SWAGGER_SECURITY_SCHEME_NAME;
import static com.cdtphuhoi.oun_de_de.utils.Utils.cambodiaNow;
import static com.cdtphuhoi.oun_de_de.utils.Utils.paddingZero;
import com.cdtphuhoi.oun_de_de.common.VehicleType;
import com.cdtphuhoi.oun_de_de.entities.Contact;
import com.cdtphuhoi.oun_de_de.entities.Customer;
import com.cdtphuhoi.oun_de_de.entities.DefaultProductSetting;
import com.cdtphuhoi.oun_de_de.entities.Product;
import com.cdtphuhoi.oun_de_de.entities.ProductSetting;
import com.cdtphuhoi.oun_de_de.entities.ProductSettingId;
import com.cdtphuhoi.oun_de_de.entities.User;
import com.cdtphuhoi.oun_de_de.entities.Vehicle;
import com.cdtphuhoi.oun_de_de.repositories.CustomerRepository;
import com.cdtphuhoi.oun_de_de.repositories.ProductRepository;
import com.cdtphuhoi.oun_de_de.repositories.ProductSettingRepository;
import com.cdtphuhoi.oun_de_de.utils.ControllerUtils;
import com.cdtphuhoi.oun_de_de.utils.ExcelUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

@Slf4j
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = SWAGGER_SECURITY_SCHEME_NAME)
@RequestMapping("/api/v1/admin")
public class SeedController {

    private final ControllerUtils controllerUtils;

    private final ProductRepository productRepository;

    private final CustomerRepository customerRepository;

    private final ProductSettingRepository productSettingRepository;

    @PostMapping("/seed")
    @SneakyThrows
    @Transactional
    public ResponseEntity<String> seed() {
        var usr = controllerUtils.getCurrentSignedInUser();
        long maxCurrentRefCode = Optional.ofNullable(productRepository.findMaxRefNo(usr.getOrgId()))
            .orElse(0L);
        var cleanIce = productRepository.findOneByNameAndOrgId("អនាម័យ", usr.getOrgId())
            .orElseGet(() -> productRepository.save(
                Product.builder()
                    .name("អនាម័យ")
                    .refNo(String.format("PROD%s", paddingZero(BigInteger.valueOf(maxCurrentRefCode + 1), DEFAULT_PADDING_LENGTH)))
                    .defaultProductSetting(
                        DefaultProductSetting.builder()
                            .price(BigDecimal.valueOf(2000))
                            .quantity(BigDecimal.valueOf(20))
                            .orgId(usr.getOrgId())
                            .build()
                    )
                    .date(cambodiaNow())
                    .orgId(usr.getOrgId())
                    .build()
            ));
        var solidIce = productRepository.findOneByNameAndOrgId("ដើម", usr.getOrgId())
            .orElseGet(() -> productRepository.save(
                Product.builder()
                    .name("ដើម")
                    .refNo(String.format("PROD%s", paddingZero(BigInteger.valueOf(maxCurrentRefCode + 2), DEFAULT_PADDING_LENGTH)))
                    .defaultProductSetting(
                        DefaultProductSetting.builder()
                            .price(BigDecimal.valueOf(6500))
                            .quantity(BigDecimal.valueOf(84))
                            .orgId(usr.getOrgId())
                            .build()
                    )
                    .date(cambodiaNow())
                    .orgId(usr.getOrgId())
                    .build()
            ));

        try (
            var inputStream = Files.newInputStream(Path.of("/Users/I753911/Desktop/projects/Customer_List_11-03-2026.xlsx"));
            var workbook = new XSSFWorkbook(inputStream)
        ) {
            var sheet = workbook.getSheet("Sheet1");
            if (sheet == null) {
                throw new IOException("Sheet not found");
            }
            var startRowIndex = 8;
            var customers = IntStream.rangeClosed(startRowIndex, sheet.getLastRowNum())
                .mapToObj(rowIndex -> mapToCustomer(
                        ExcelUtils.getOrCreateRow(sheet, rowIndex),
                        usr,
                        cleanIce,
                        solidIce
                    )
                )
                .toList();
            log.info("customers={}", customers);
        } catch (IOException e) {
            log.error("err: ", e);
        }
        return ResponseEntity
            .status(HttpStatus.OK)
            .body("success");
    }

    private Customer mapToCustomer(
        Row row,
        User usr,
        Product cleanIce,
        Product solidIce
    ) {
        var licencePlate = ExcelUtils.getCellStringValue(row.getCell(2));
        VehicleType vehicleType;
        if (licencePlate.length() <= 4) {
            vehicleType = VehicleType.TUK_TUK;
        } else {
            vehicleType = VehicleType.TRUCK;
        }
        var customer = Customer.builder()
            .code(ExcelUtils.getCellStringValue(row.getCell(1)))
            .name(ExcelUtils.getCellStringValue(row.getCell(3)))
            .contact(
                Contact.builder()
                    .address(ExcelUtils.getCellStringValue(row.getCell(9)))
                    .telephone(ExcelUtils.getCellStringValue(row.getCell(10)))
                    .orgId(usr.getOrgId())
                    .build()
            )
            .registerDate(cambodiaNow())
            .status(true)
            .employee(usr)
            .orgId(usr.getOrgId())
            .build();
        var vehicle = Vehicle.builder()
            .vehicleType(vehicleType)
            .licensePlate(licencePlate)
            .customer(customer)
            .orgId(usr.getOrgId())
            .build();
        customer.setVehicles(List.of(vehicle));
        var cleanIcePrice = ExcelUtils.getCellStringValue(row.getCell(4));
        var cleanIceQuantity = ExcelUtils.getCellStringValue(row.getCell(6));
        var solidIcePrice = ExcelUtils.getCellStringValue(row.getCell(5));
        var solidIceQuantity = ExcelUtils.getCellStringValue(row.getCell(7));
        var customerDb = customerRepository.save(customer);
        log.info("Saved customer [code={}]", customerDb.getCode());
        var prodSettings = List.of(
            ProductSetting.builder()
                .price(cleanIcePrice == null || cleanIcePrice.equals("N/A") ? null : new BigDecimal(cleanIcePrice))
                .quantity(cleanIceQuantity == null || cleanIceQuantity.equals("N/A") ? null : new BigDecimal(cleanIceQuantity))
                .productSettingId(
                    ProductSettingId.builder()
                        .customerId(customerDb.getId())
                        .productId(cleanIce.getId())
                        .build()
                )
                .customer(customerDb)
                .product(cleanIce)
                .orgId(usr.getOrgId())
                .build(),
            ProductSetting.builder()
                .price(solidIcePrice == null || solidIcePrice.equals("N/A") ? null : new BigDecimal(solidIcePrice))
                .quantity(solidIceQuantity == null || solidIceQuantity.equals("N/A") ? null : new BigDecimal(solidIceQuantity))
                .productSettingId(
                    ProductSettingId.builder()
                        .customerId(customerDb.getId())
                        .productId(solidIce.getId())
                        .build()
                )
                .customer(customerDb)
                .product(solidIce)
                .orgId(usr.getOrgId())
                .build()
        );
        productSettingRepository.saveAll(prodSettings);
        log.info("Saved prod settings for customer [code={}]", customerDb.getCode());
        return customer;
    }
}
