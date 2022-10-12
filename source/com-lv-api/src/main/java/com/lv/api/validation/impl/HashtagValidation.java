package com.lv.api.validation.impl;

import com.lv.api.validation.Hashtag;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class HashtagValidation implements ConstraintValidator<Hashtag, String> {

    private boolean allowNull;

    @Override
    public void initialize(Hashtag constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(String hashtag, ConstraintValidatorContext constraintValidatorContext) {
        if (hashtag == null && allowNull)
            return true;
        if(hashtag != null)
            return hashtag.matches("^#[\\w_]+(?:\\s+#[\\w_]+)*$");
        return false;
    }
}
