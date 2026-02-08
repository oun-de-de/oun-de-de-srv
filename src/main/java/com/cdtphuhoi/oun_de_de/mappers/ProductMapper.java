package com.cdtphuhoi.oun_de_de.mappers;

import com.cdtphuhoi.oun_de_de.controllers.dto.product.CreateProductRequest;
import com.cdtphuhoi.oun_de_de.controllers.dto.product.UpdateProductRequest;
import com.cdtphuhoi.oun_de_de.entities.DefaultProductSetting;
import com.cdtphuhoi.oun_de_de.entities.Product;
import com.cdtphuhoi.oun_de_de.entities.User;
import com.cdtphuhoi.oun_de_de.services.product.dto.CreateProductData;
import com.cdtphuhoi.oun_de_de.services.product.dto.DefaultProductSettingResult;
import com.cdtphuhoi.oun_de_de.services.product.dto.ProductResult;
import com.cdtphuhoi.oun_de_de.services.product.dto.UpdateProductData;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(
    uses = {
        SettingMapper.class
    },
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductResult toProductResult(Product product);

    List<ProductResult> toListProductResults(List<Product> product);

    CreateProductData toCreateProductData(CreateProductRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orgId", source = "employeeUser.orgId")
    Product toProduct(CreateProductData request, User employeeUser);

    @AfterMapping
    default void afterMapping(
        @MappingTarget Product product,
        CreateProductData request
    ) {
        product.setDefaultProductSetting(toDefaultProductSetting(request, product));
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orgId", source = "product.orgId")
    @Mapping(target = "price", source = "request.defaultPrice")
    @Mapping(target = "quantity", source = "request.defaultQuantity")
    DefaultProductSetting toDefaultProductSetting(CreateProductData request, Product product);

    UpdateProductData toUpdateProductData(UpdateProductRequest request);

    void updateProduct(
        @MappingTarget Product product,
        UpdateProductData updateProductData
    );

    DefaultProductSettingResult toDefaultProductSettingResult(DefaultProductSetting defaultProductSetting);
}
