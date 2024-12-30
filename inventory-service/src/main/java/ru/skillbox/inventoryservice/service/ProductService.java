package ru.skillbox.inventoryservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.skillbox.inventoryservice.domain.Product;
import ru.skillbox.inventoryservice.dto.ErrorKafkaDto;
import ru.skillbox.inventoryservice.dto.InventoryKafkaDto;
import ru.skillbox.inventoryservice.dto.product.ProductFilter;
import ru.skillbox.inventoryservice.dto.product.UpsertProductRequest;

public interface ProductService {

    Page<Product> getAll(ProductFilter productFilter, Pageable pageable);

    Product getById(Long id);

    Product create(UpsertProductRequest request);

    Product update(long id, UpsertProductRequest request);

    void deleteById(Long id);

    void checkProductAvailability(InventoryKafkaDto inventoryKafkaDto);

    void returnGoods(ErrorKafkaDto errorKafkaDto);
}