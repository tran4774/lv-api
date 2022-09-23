package com.lv.api.storage.criteria;

import com.lv.api.storage.model.Location;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class LocationCriteria {
    private Long id;

    public Specification<Location> getSpecification() {
        return (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (getId() != null) {
                predicates.add(cb.equal(root.get("id"), getId()));
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
