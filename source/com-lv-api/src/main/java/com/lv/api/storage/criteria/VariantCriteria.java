package com.lv.api.storage.criteria;

import com.lv.api.storage.model.Variant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class VariantCriteria {
    private Long id;
    private Integer kind;
    private String name;
    private String value;
    private Double price;
    private Long variantTemplateId;

    public Specification<Variant> getSpecification() {
        return (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (getId() != null) {
                predicates.add(cb.equal(root.get("id"), getId()));
            }

            if (getKind() != null) {
                predicates.add(cb.equal(root.get("kind"), getKind()));
            }

            if (getName() != null) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + getName().toLowerCase() + "%"));
            }

            if (getValue() != null) {
                predicates.add(cb.like(cb.lower(root.get("value")), getValue()));
            }

            if (getPrice() != null) {
                predicates.add(cb.equal(root.get("price"), getPrice()));
            }

            if (getVariantTemplateId() != null) {
                predicates.add(cb.equal(root.get("variant_template_id"), getVariantTemplateId()));
            }

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
