package com.cdtphuhoi.oun_de_de.services.product;

import com.cdtphuhoi.oun_de_de.entities.User;
import com.cdtphuhoi.oun_de_de.repositories.ProductRepository;
import com.cdtphuhoi.oun_de_de.services.OrgManagementService;
import com.cdtphuhoi.oun_de_de.services.product.dto.CreateProductData;
import com.cdtphuhoi.oun_de_de.services.product.dto.ProductResult;
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
public class ProductService implements OrgManagementService {

    private final ProductRepository productRepository;

    public List<ProductResult> findAll() {
        var result = productRepository.findAll();
        return MapperHelpers.getProductMapper().toListProductResults(result);
    }

    public ProductResult createProduct(CreateProductData createProductData, User usr) {
        var product = MapperHelpers.getProductMapper().toProduct(createProductData, usr);
        log.info("Creating product");
        var productDb = productRepository.save(product);
        log.info("Created product, id = {}", productDb.getId());
        return MapperHelpers.getProductMapper().toProductResult(productDb);
    }
}
