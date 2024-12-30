package ru.skillbox.inventoryservice.repository;

import org.springframework.data.jpa.domain.Specification;
import ru.skillbox.inventoryservice.domain.Category;
import ru.skillbox.inventoryservice.domain.Product;
import ru.skillbox.inventoryservice.dto.product.ProductFilter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

public interface ProductSpecifications {

    static Specification<Product> withFilter(ProductFilter productFilter) {

        return Specification.where(byCategory(productFilter.getCategoryTitle()));
    }

    static Specification<Product> byCategory(String categoryTitle) {

        return(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Join<Category, Product> productCategory = root.join("category");
            return criteriaBuilder.equal(productCategory.get("title"), categoryTitle);
        };
    }
}
