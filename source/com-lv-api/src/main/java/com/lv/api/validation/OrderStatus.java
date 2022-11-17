package com.lv.api.validation;

import com.lv.api.validation.impl.OrderStatusValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OrderStatusValidation.class)
@Documented
public @interface OrderStatus {
    boolean allowNull() default false;

    String message() default "Order status invalid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
