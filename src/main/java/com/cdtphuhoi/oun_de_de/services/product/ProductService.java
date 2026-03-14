package com.cdtphuhoi.oun_de_de.services.product;

import static com.cdtphuhoi.oun_de_de.common.Constants.DEFAULT_PADDING_LENGTH;
import static com.cdtphuhoi.oun_de_de.utils.Utils.cambodiaNow;
import static com.cdtphuhoi.oun_de_de.utils.Utils.paddingZero;
import com.cdtphuhoi.oun_de_de.entities.Product_;
import com.cdtphuhoi.oun_de_de.entities.User;
import com.cdtphuhoi.oun_de_de.exceptions.BadRequestException;
import com.cdtphuhoi.oun_de_de.exceptions.ResourceNotFoundException;
import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import com.cdtphuhoi.oun_de_de.repositories.ProductRepository;
import com.cdtphuhoi.oun_de_de.repositories.UnitRepository;
import com.cdtphuhoi.oun_de_de.services.OrgManagementService;
import com.cdtphuhoi.oun_de_de.services.product.dto.CreateProductData;
import com.cdtphuhoi.oun_de_de.services.product.dto.ProductResult;
import com.cdtphuhoi.oun_de_de.services.product.dto.UpdateProductData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.criteria.JoinType;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductService implements OrgManagementService {

    private final ProductRepository productRepository;

    private final UnitRepository unitRepository;

    public List<ProductResult> findAll() {
        var result = productRepository.findAll(
            Specification.allOf(
                (root, query, cb) -> {
                    root.fetch(Product_.DEFAULT_PRODUCT_SETTING, JoinType.LEFT);
                    root.fetch(Product_.UNIT, JoinType.LEFT);
                    return null;
                }
            ),
            Sort.by(Sort.Direction.DESC, Product_.DATE)
        );
        return MapperHelpers.getProductMapper().toListProductResults(result);
    }

    public ProductResult createProduct(CreateProductData createProductData, User usr) {
        if (!createProductData.getIsPackagedByQuantity() && createProductData.getDefaultQuantity() != null) {
            throw new BadRequestException("Product is not packaged, should not config quantity");
        }
        if (createProductData.getIsPackagedByQuantity() && createProductData.getDefaultQuantity() == null) {
            throw new BadRequestException("Product is packaged, must config quantity");
        }
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
        var maxCurrentRefCode = Optional.ofNullable(productRepository.findMaxRefNo(usr.getOrgId()))
            .orElse(0L);
        product.setRefNo(String.format("PROD%s", paddingZero(BigInteger.valueOf(maxCurrentRefCode + 1), DEFAULT_PADDING_LENGTH)));
        product.setDate(cambodiaNow());
        log.info("Creating product");
        var productDb = productRepository.save(product);
        log.info("Created product, id = {}", productDb.getId());
        return MapperHelpers.getProductMapper().toProductResult(productDb);
    }

    public ProductResult updateProduct(String productId, UpdateProductData updateProductData) {
        var product = productRepository.findOne(
                Specification.allOf(
                    (root, query, cb) -> cb.equal(root.get(Product_.ID), productId),
                    (root, query, cb) -> {
                        root.fetch(Product_.DEFAULT_PRODUCT_SETTING, JoinType.LEFT);
                        root.fetch(Product_.UNIT, JoinType.LEFT);
                        return null;
                    }
                )
            )
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

        if (updateProductData.getIsPackagedByQuantity() != null) {
            if (Boolean.FALSE.equals(product.getIsPackagedByQuantity())) {
                product.getDefaultProductSetting().setQuantity(null);
            }
            if (Boolean.TRUE.equals(product.getIsPackagedByQuantity()) &&
                updateProductData.getDefaultQuantity() == null &&
                product.getDefaultProductSetting().getQuantity() == null) {
                throw new BadRequestException(
                    String.format("Product [id=%s] is packaged, must config quantity", product.getId())
                );
            }
        }

        var updatedProduct = productRepository.save(product);
        return MapperHelpers.getProductMapper().toProductResult(updatedProduct);
    }
}
