package ru.skillbox.inventoryservice.service;

import ru.skillbox.inventoryservice.domain.Category;
import ru.skillbox.inventoryservice.dto.category.UpsertCategoryRequest;

import java.util.List;

public interface CategoryService {

    List<Category> getAll();

    Category getById(Long id);

    Category getByTitle(String title);

    Category create (UpsertCategoryRequest request);

    Category update (Long id, UpsertCategoryRequest request);

    void deleteById(Long id);
}