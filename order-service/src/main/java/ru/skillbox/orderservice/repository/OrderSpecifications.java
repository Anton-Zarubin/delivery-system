package ru.skillbox.orderservice.repository;

import org.springframework.data.jpa.domain.Specification;
import ru.skillbox.orderservice.domain.Order;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public interface OrderSpecifications {

    static Specification<Order> byUser(Long userId) {

        return(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("userId"), userId);
    }
}
