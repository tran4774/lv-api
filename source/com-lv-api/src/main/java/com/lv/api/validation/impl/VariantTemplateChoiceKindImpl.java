package com.lv.api.validation.impl;

import com.lv.api.constant.Constants;
import com.lv.api.validation.VariantTemplateChoiceKind;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class VariantTemplateChoiceKindImpl implements ConstraintValidator<VariantTemplateChoiceKind, Integer> {

    private boolean allowNull;

    @Override
    public void initialize(VariantTemplateChoiceKind constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(Integer choiceKind, ConstraintValidatorContext constraintValidatorContext) {
        if (choiceKind == null && allowNull) {
            return true;
        }

        if (choiceKind != null) {
            switch (choiceKind) {
                case Constants.VARIANT_TEMPLATE_SINGLE_CHOICE:
                case Constants.VARIANT_TEMPLATE_MULTIPLE_CHOICE:
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }
}
