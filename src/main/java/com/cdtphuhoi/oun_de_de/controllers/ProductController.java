package com.cdtphuhoi.oun_de_de.controllers;

import static com.cdtphuhoi.oun_de_de.common.Constants.SWAGGER_SECURITY_SCHEME_NAME;
import com.cdtphuhoi.oun_de_de.controllers.dto.product.CreateProductRequest;
import com.cdtphuhoi.oun_de_de.services.product.ProductService;
import com.cdtphuhoi.oun_de_de.services.product.dto.ProductResult;
import com.cdtphuhoi.oun_de_de.utils.ControllerUtils;
import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
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
import jakarta.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = SWAGGER_SECURITY_SCHEME_NAME)
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    private final ControllerUtils controllerUtils;

    @GetMapping
    public ResponseEntity<List<ProductResult>> listProducts() {
        return ResponseEntity.ok(
            productService.findAll()
        );
    }

    @PostMapping
    public ResponseEntity<ProductResult> createProduct(
        @Valid @RequestBody CreateProductRequest request) {
        var usr = controllerUtils.getCurrentSignedInUser();
        var result = productService.createProduct(
            MapperHelpers.getProductMapper().toCreateProductData(request),
            usr
        );
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(result);
    }
}
