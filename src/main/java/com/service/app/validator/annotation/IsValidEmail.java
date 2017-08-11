package com.service.app.validator.annotation;

import com.service.app.validator.annotation.impl.EmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailValidator.class)
@Target( {ElementType.FIELD} )
@Retention(RetentionPolicy.RUNTIME)
public @interface IsValidEmail {

    String message() default "Please enter a valid e-mail address.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
