package ru.skillbox.inventoryservice.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.skillbox.inventoryservice.repository.*;
import ru.skillbox.inventoryservice.service.*;
import ru.skillbox.inventoryservice.service.impl.CategoryServiceImpl;
import ru.skillbox.inventoryservice.service.impl.ProductServiceImpl;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestConfig {

    @Bean
    public CategoryRepository categoryRepository() {
        return mock(CategoryRepository.class);
    }

    @Bean
    public ProductRepository productRepository() {
        return mock(ProductRepository.class);
    }

    @Bean
    public InvoiceRepository invoiceRepository() {
        return mock(InvoiceRepository.class);
    }

    @Bean
    public KafkaService kafkaService() {
        return mock(KafkaService.class);
    }

    @Bean
    public CategoryService categoryService() {
        return new CategoryServiceImpl(categoryRepository());
    }

    @Bean
    public ProductService productService() {
        return new ProductServiceImpl(productRepository(), categoryService(), kafkaService(), invoiceRepository());
    }
}