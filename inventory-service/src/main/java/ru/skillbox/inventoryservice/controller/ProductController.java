package ru.skillbox.inventoryservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.inventoryservice.dto.product.*;
import ru.skillbox.inventoryservice.mapper.ProductMapper;
import ru.skillbox.inventoryservice.service.ProductService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Get all products")
    @GetMapping("/view")
    public ResponseEntity<ProductListResponse> getAllProducts(ProductFilter filter, Pageable pageable) {
        return ResponseEntity.ok()
                .body(ProductMapper.INSTANCE.productListToProductListResponse(
                        productService.getAll(filter, pageable))
                );
    }

    @Operation(summary = "Get product by id")
    @GetMapping("/view/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(ProductMapper.INSTANCE.productToResponse(productService.getById(id)));
    }

    @Operation(summary = "Add new product", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody UpsertProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ProductMapper.INSTANCE.productToResponse(productService.create(request)));
    }

    @Operation(summary = "Update product", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody UpsertProductRequest request) {
        return ResponseEntity.ok(ProductMapper.INSTANCE.productToResponse(productService.update(id, request)));
    }

    @Operation(summary = "Delete product", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
