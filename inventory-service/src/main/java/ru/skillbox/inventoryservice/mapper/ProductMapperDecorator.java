package ru.skillbox.inventoryservice.mapper;

import ru.skillbox.inventoryservice.domain.Product;
import ru.skillbox.inventoryservice.dto.product.UpsertProductRequest;
import ru.skillbox.inventoryservice.service.CategoryService;

public abstract class ProductMapperDecorator implements ProductMapper {

    @Override
    public Product requestToProduct(UpsertProductRequest request, CategoryService categoryService) {
        Product product = new Product();
        product.setTitle(request.getTitle());
        product.setDescription(request.getDescription());
        product.setUnitPrice(request.getUnitPrice());
        product.setCount(request.getCount());
        product.setCategory(categoryService.getById(request.getCategoryId()));

        return product;
    }

    @Override
    public Product requestToProduct(Long id, UpsertProductRequest request, CategoryService categoryService) {
        Product product = requestToProduct(request, categoryService);
        product.setId(id);

        return product;
    }

}
