package ru.skillbox.inventoryservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.inventoryservice.dto.category.CategoryListResponse;
import ru.skillbox.inventoryservice.dto.category.CategoryResponse;
import ru.skillbox.inventoryservice.dto.category.UpsertCategoryRequest;
import ru.skillbox.inventoryservice.mapper.CategoryMapper;
import ru.skillbox.inventoryservice.service.CategoryService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Get all categories")
    @GetMapping("/view")
    public ResponseEntity<CategoryListResponse> getAllCategories() {
        return ResponseEntity.ok(CategoryMapper.INSTANCE.categoryListToCategoryListResponse(categoryService.getAll()));
    }

    @Operation(summary = "Get category by id")
    @GetMapping("/view/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(CategoryMapper.INSTANCE.categoryToResponse(categoryService.getById(id)));
    }

    @Operation(summary = "Add new category", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<CategoryResponse> createCategory(UpsertCategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CategoryMapper.INSTANCE.categoryToResponse(categoryService.create(request)));
    }

    @Operation(summary = "Update category", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long id, @Valid @RequestBody UpsertCategoryRequest request) {
        return ResponseEntity.ok(CategoryMapper.INSTANCE.categoryToResponse(categoryService.update(id, request)));
    }

    @Operation(summary = "Delete category", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
