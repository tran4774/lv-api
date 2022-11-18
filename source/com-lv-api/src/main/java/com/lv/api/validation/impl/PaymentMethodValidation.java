package com.lv.api.validation.impl;

import com.lv.api.constant.Constants;
import com.lv.api.validation.PaymentMethod;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PaymentMethodValidation implements ConstraintValidator<PaymentMethod, Integer> {

    private boolean allowNull;

    @Override
    public void initialize(PaymentMethod constraintAnnotation) {
        allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(Integer paymentMethod, ConstraintValidatorContext constraintValidatorContext) {
        if (paymentMethod == null && allowNull) {
            return true;
        }
        if (paymentMethod != null) {
            switch (paymentMethod) {
                case Constants.PAYMENT_METHOD_CARD:
                case Constants.PAYMENT_METHOD_PAYPAL:
                case Constants.PAYMENT_METHOD_COD:
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }
}
