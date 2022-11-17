package com.lv.api.validation.impl;

import com.lv.api.constant.Constants;
import com.lv.api.validation.OrderStatus;
import com.lv.api.validation.PaymentMethod;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OrderStatusValidation implements ConstraintValidator<OrderStatus, Integer> {

    private boolean allowNull;

    @Override
    public void initialize(OrderStatus constraintAnnotation) {
        allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(Integer orderStatus, ConstraintValidatorContext constraintValidatorContext) {
        if (orderStatus == null && allowNull) {
            return true;
        }
        if (orderStatus != null) {
            switch (orderStatus) {
                case Constants.ORDER_STATUS_NEW:
                case Constants.ORDER_STATUS_CHECKOUT:
                case Constants.ORDER_STATUS_PAID:
                case Constants.ORDER_STATUS_FAILED:
                case Constants.ORDER_STATUS_DELIVERED:
                case Constants.ORDER_STATUS_RETURNED:
                case Constants.ORDER_STATUS_COMPLETED:
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }
}
