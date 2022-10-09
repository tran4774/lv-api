package com.lv.api.validation;

import com.lv.api.validation.impl.VariantTemplateChoiceKindImpl;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = VariantTemplateChoiceKindImpl.class)
@Documented
public @interface VariantTemplateChoiceKind {
    boolean allowNull() default false;
    String message() default  "Variant template choice kind is invalid.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
