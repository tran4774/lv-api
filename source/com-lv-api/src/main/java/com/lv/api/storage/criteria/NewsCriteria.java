package com.lv.api.storage.criteria;

import com.lv.api.constant.Constants;
import com.lv.api.storage.model.Category;
import com.lv.api.storage.model.News;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class NewsCriteria {
    private Long id;
    private Integer kind;
    private Long categoryId;
    private Integer status;
    private String title;
    private Integer pinTop;
    private List<String> tags;

    public Specification<News> getSpecification() {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<News> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if(getId() != null) {
                    predicates.add(cb.equal(root.get("id"), getId()));
                }

                if(getCategoryId() != null) {
                    Join<News, Category> joinCategory = root.join("category", JoinType.INNER);
                    predicates.add(cb.equal(joinCategory.get("id"), getCategoryId()));
                }

                if(getKind() != null) {
                    predicates.add(cb.equal(root.get("kind"), getKind()));
                }

                if(getStatus() != null) {
                    predicates.add(cb.equal(root.get("status"), getStatus()));
                }

                if(getPinTop() != null) {
                    predicates.add(cb.equal(root.get("pinTop"), getPinTop()));
                }

                if(getTitle() != null) {
                    predicates.add(cb.like(root.get("title"), "%" + getTitle().toLowerCase() + "%"  ));
                }

                if (getTags() != null) {
                    setTags(getTags().stream().map(tag -> tag.toLowerCase().trim()).collect(Collectors.toList()));
                    List<Predicate> predicatesOr = new ArrayList<>();
                    for (String tag : tags) {
                        predicatesOr.add(cb.like(cb.lower(root.get("tags")), "%" + tag + "%"));
                    }
                    predicates.add(cb.or(predicatesOr.toArray(new Predicate[]{})));
                }

                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

    public Specification<News> getSpecificationGuest() {
        return (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(getCategoryId() != null) {
                Join<News, Category> joinCategory = root.join("category", JoinType.INNER);
                predicates.add(cb.equal(joinCategory.get("id"), getCategoryId()));
            }

            if(getTitle() != null) {
                predicates.add(cb.like(root.get("title"), "%" + getTitle().toLowerCase() + "%"  ));
            }

            if(getKind() != null) {
                predicates.add(cb.equal(root.get("kind"), getKind()));
            }

            if (getTags() != null) {
                setTags(getTags().stream().map(tag -> tag.toLowerCase().trim()).collect(Collectors.toList()));
                List<Predicate> predicatesOr = new ArrayList<>();
                for (String tag : tags) {
                    predicatesOr.add(cb.like(cb.lower(root.get("tags")), "%" + tag + "%"));
                }
                predicates.add(cb.or(predicatesOr.toArray(new Predicate[]{})));
            }

            predicates.add(cb.equal(root.get("status"), Constants.STATUS_ACTIVE));

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}