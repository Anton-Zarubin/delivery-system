package ru.skillbox.inventoryservice.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import ru.skillbox.inventoryservice.domain.Product;
import ru.skillbox.inventoryservice.dto.product.*;
import ru.skillbox.inventoryservice.service.CategoryService;

import java.util.List;

@DecoratedWith(ProductMapperDecorator.class)
@Mapper(uses = CategoryMapper.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    Product requestToProduct(UpsertProductRequest request, @Context CategoryService categoryService);

    Product requestToProduct(Long id, UpsertProductRequest request, @Context CategoryService categoryService);

    ProductResponse productToResponse(Product product);

    List<ProductResponse> productListToResponseList(List<Product> products);

    default ProductListResponse productListToProductListResponse(Page<Product> products) {
        return new ProductListResponse(products.getTotalElements(),productListToResponseList(products.getContent()));
    }
}
