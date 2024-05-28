package com.vashchenko.task.repositories.criteria;

import com.vashchenko.task.dto.views.ClientBaseProjection;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class ClientCriteria {

    public static Specification<ClientBaseProjection> birthDateAfter(Date birthDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("birthDate"), birthDate);
    }

    public static Specification<ClientBaseProjection> phoneEquals(String phone) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("phone"), phone);
    }

    public static Specification<ClientBaseProjection> fullNameLike(String fullName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("fullName"), fullName + "%");
    }

    public static Specification<ClientBaseProjection> emailEquals(String email) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("email"), email);
    }
}