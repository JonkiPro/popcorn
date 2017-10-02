package com.common.dto.request.validator;

import com.common.dto.request.validator.impl.EmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Validate e-mail address syntax.
 */
@Documented
@Constraint(validatedBy = EmailValidator.class)
@Target( {ElementType.FIELD} )
@Retention(RetentionPolicy.RUNTIME)
public @interface IsValidEmail {

    String message() default "Please enter a valid e-mail address.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
