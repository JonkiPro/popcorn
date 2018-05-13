package com.jonki.popcorn.common.dto.request.validator;

import com.jonki.popcorn.common.dto.request.validator.impl.PasswordsEqualConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Check whether the passwords are the same.
 */
@Documented
@Constraint(validatedBy = PasswordsEqualConstraintValidator.class)
@Target( {ElementType.TYPE} )
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordsEqualConstraint {

    String message() default "Passwords are not equal.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
