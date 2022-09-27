package com.lv.api.storage.criteria;

import com.lv.api.storage.model.Account;
import com.lv.api.storage.model.Customer;
import com.lv.api.validation.Gender;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CustomerCriteria {
    private Long id;
    private String fullName;
    @Gender
    private Integer gender;
    private LocalDate birthday;
    private String note;

    public Specification<Customer> getSpecification() {
        return (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<Customer, Account> joinAccount = root.join("account", JoinType.INNER);

            if (getId() != null) {
                predicates.add(cb.equal(root.get("id"), getId()));
            }

            if (getFullName() != null) {
                predicates.add(cb.like(joinAccount.get("fullName"), "%" + getFullName().toLowerCase() + "%"));
            }

            if (getGender() != null) {
                predicates.add(cb.equal(root.get("gender"), getGender()));
            }

            if (getBirthday() != null) {
                predicates.add(cb.equal(root.get("birthday"), getBirthday()));
            }

            if (getNote() != null) {
                predicates.add(cb.like(root.get("fullName"), "%" + getNote().toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
