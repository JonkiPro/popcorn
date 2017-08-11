package com.service.app.validator.annotation;

import com.service.app.validator.annotation.impl.PasswordIsTheSameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordIsTheSameValidator.class)
@Target( {ElementType.FIELD} )
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordIsTheSame {

    String message() default "Password is incorrect.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
