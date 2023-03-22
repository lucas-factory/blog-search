package com.lucas.platform.searchcore.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {PublisherTypeValidator.class})
public @interface PublisherType {
    String message() default "Invalid web publisher type";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
