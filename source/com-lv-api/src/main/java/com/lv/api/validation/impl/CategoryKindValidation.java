package com.lv.api.validation.impl;

import com.lv.api.constant.LandingISConstant;
import com.lv.api.validation.CategoryKind;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class CategoryKindValidation implements ConstraintValidator<CategoryKind, Integer> {
    private boolean allowNull;

    @Override
    public void initialize(CategoryKind constraintAnnotation) { allowNull = constraintAnnotation.allowNull(); }

    @Override
    public boolean isValid(Integer categoryKind, ConstraintValidatorContext constraintValidatorContext) {
        if(categoryKind == null && allowNull) {
            return true;
        }
        if(!Objects.equals(categoryKind, LandingISConstant.CATEGORY_KIND)) {
            return false;
        }
        return true;
    }
}
