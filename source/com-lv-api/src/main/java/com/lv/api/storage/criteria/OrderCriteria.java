package com.lv.api.storage.criteria;

import com.lv.api.storage.model.Order;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderCriteria {

    public Specification<Order> getSpecification() {
        return (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();


            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}
