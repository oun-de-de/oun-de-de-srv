package com.cdtphuhoi.oun_de_de.services.product;

import com.cdtphuhoi.oun_de_de.entities.User;
import com.cdtphuhoi.oun_de_de.exceptions.ResourceNotFoundException;
import com.cdtphuhoi.oun_de_de.repositories.ProductRepository;
import com.cdtphuhoi.oun_de_de.repositories.UnitRepository;
import com.cdtphuhoi.oun_de_de.services.OrgManagementService;
import com.cdtphuhoi.oun_de_de.services.product.dto.CreateProductData;
import com.cdtphuhoi.oun_de_de.services.product.dto.ProductResult;
import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import com.cdtphuhoi.oun_de_de.services.product.dto.UpdateProductData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductService implements OrgManagementService {

    private final ProductRepository productRepository;

    private final UnitRepository unitRepository;

    public List<ProductResult> findAll() {
        var result = productRepository.findAll();
        return MapperHelpers.getProductMapper().toListProductResults(result);
    }

    public ProductResult createProduct(CreateProductData createProductData, User usr) {
        var unit = Optional.ofNullable(createProductData.getUnitId())
            .map(unitId -> unitRepository.findOneById(unitId)
                .orElseThrow(
                    () -> new ResourceNotFoundException(
                        String.format("Unit [id=%s] not found", createProductData.getUnitId())
                    )
                )
            )
            .orElse(null);
        var product = MapperHelpers.getProductMapper().toProduct(createProductData, usr);
        product.setUnit(unit);
        log.info("Creating product");
        var productDb = productRepository.save(product);
        log.info("Created product, id = {}", productDb.getId());
        return MapperHelpers.getProductMapper().toProductResult(productDb);
    }

    public ProductResult updateProduct(String productId, UpdateProductData updateProductData) {
        var product = productRepository.findOneById(productId)
            .orElseThrow(
                () -> new ResourceNotFoundException(
                    String.format("Product [id=%s] not found", productId)
                )
            );
        if (updateProductData.getUnitId() != null &&
            (product.getUnit() == null ||
                !product.getUnit().getId().equals(updateProductData.getUnitId()))
        ) {
            var unit = unitRepository.findOneById(updateProductData.getUnitId())
                .orElseThrow(
                    () -> new ResourceNotFoundException(
                        String.format("Unit [id=%s] not found", updateProductData.getUnitId())
                    )
                );
            product.setUnit(unit);
        }
        MapperHelpers.getProductMapper().updateProduct(product, updateProductData);
        var updatedProduct = productRepository.save(product);
        return MapperHelpers.getProductMapper().toProductResult(updatedProduct);
    }
}
