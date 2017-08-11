package com.service.app.validator.annotation;

import com.service.app.validator.annotation.impl.PasswordIsDifferentValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordIsDifferentValidator.class)
@Target( {ElementType.FIELD} )
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordIsDifferent {

    String message() default "Password is the same.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
