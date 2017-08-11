package com.service.app.validator.annotation;

import com.service.app.validator.annotation.impl.ExistsEmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExistsEmailValidator.class)
@Target( {ElementType.FIELD} )
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistsEmail {

    boolean ifExistsReturn() default true;

    String message() default "E-mail exists in database.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
