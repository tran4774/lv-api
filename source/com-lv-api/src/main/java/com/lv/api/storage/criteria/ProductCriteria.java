package com.lv.api.storage.criteria;

import com.lv.api.storage.model.Product;
import com.lv.api.storage.model.ProductConfig;
import com.lv.api.validation.ProductKind;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductCriteria {
    private Long id;
    private Long categoryId;
    private String tags;
    private String description;
    private String name;
    private Double fromPrice;
    private Double toPrice;
    private Boolean isSoldOut;
    private Long productParent;
    @ProductKind
    private Integer kind;
    private String variantName;

    public Specification<Product> getSpecification() {
        return (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (getId() != null) {
                predicates.add(cb.equal(root.get("id"), getId()));
            }

            if (getCategoryId() != null) {
                predicates.add(cb.equal(root.get("categoryId"), getCategoryId()));
            }

            if (getTags() != null) {
                ;
            }

            if (getDescription() != null) {
                predicates.add(cb.like(cb.lower(root.get("description")), "%" + getDescription().toLowerCase() + "%"));
            }

            if (getName() != null) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + getName().toLowerCase() + ""));
            }

            if (getFromPrice() != null) {
                predicates.add(cb.ge(root.get("price"), getFromPrice()));
            }

            if (getToPrice() != null) {
                predicates.add(cb.le(root.get("price"), getToPrice()));
            }

            if (getIsSoldOut() != null) {
                predicates.add(cb.equal(root.get("isSoldOut"), getIsSoldOut()));
            }

            if (getProductParent() != null) {
                predicates.add(cb.equal(root.get("productParent"), getProductParent()));
            }

            if (getKind() != null) {
                predicates.add(cb.equal(root.get("kind"), getKind()));
            }

            if (getVariantName() != null) {
//                Join<Product, ProductConfig> productConfigJoin = root.join("ProductConfig", JoinType.INNER);
//                predicates.add(cb.like(cb.lower(productConfigJoin.join("ProductVariant").get("name")), "%" + getVariantName().toLowerCase() + ""));
                ;
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
