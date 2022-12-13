package com.lv.api.storage.criteria;

import com.lv.api.storage.model.Order;
import com.lv.api.validation.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderCriteria {

    private Long id;
    @OrderStatus(allowNull = true)
    private Integer orderStatus;
    private String receiverFullName;
    private String phone;
    private Integer paymentMethod;
    private Long customerId;

    public Specification<Order> getSpecification() {
        return (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (getId() != null) {
                predicates.add(cb.equal(root.get("id"), getId()));
            }

            if (getOrderStatus() != null) {
                predicates.add(cb.equal(root.get("status"), getOrderStatus()));
            }

            if (StringUtils.isNoneBlank(getReceiverFullName())) {
                predicates.add(cb.like(cb.lower(root.get("receiverFullName")), "%" + getReceiverFullName().toLowerCase() + "%"));
            }

            if (StringUtils.isNoneBlank(getPhone())) {
                predicates.add(cb.like(root.get("phone"), "%" + getPhone() + "%"));
            }

            if (getPaymentMethod() != null) {
                predicates.add(cb.equal(root.get("paymentMethod"), getPaymentMethod()));
            }

            if (getCustomerId() != null) {
                predicates.add(cb.equal(root.get("customer"), getCustomerId()));
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}
