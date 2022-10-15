package com.lv.api.validation.impl;

import com.lv.api.validation.Hashtag;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class HashtagValidation implements ConstraintValidator<Hashtag, String> {

    private boolean allowNull;

    @Override
    public void initialize(Hashtag constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(String hashtag, ConstraintValidatorContext constraintValidatorContext) {
        if(hashtag != null) {
            Pattern pattern = Pattern.compile("^#[\\w_]+(?:\\s+#[\\w_]+)*$", Pattern.UNICODE_CHARACTER_CLASS);
            return pattern.matcher(hashtag).find();
        }
        return allowNull;
    }
}
