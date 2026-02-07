package com.cdtphuhoi.oun_de_de.mappers;

import com.cdtphuhoi.oun_de_de.controllers.dto.product.CreateProductRequest;
import com.cdtphuhoi.oun_de_de.entities.Product;
import com.cdtphuhoi.oun_de_de.entities.User;
import com.cdtphuhoi.oun_de_de.services.product.dto.CreateProductData;
import com.cdtphuhoi.oun_de_de.services.product.dto.ProductResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(
    uses = MapperHelpers.class
)
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductResult toProductResult(Product product);

    List<ProductResult> toListProductResults(List<Product> product);

    CreateProductData toCreateProductData(CreateProductRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orgId", source = "employeeUser.orgId")
    Product toProduct(CreateProductData request, User employeeUser);
}
