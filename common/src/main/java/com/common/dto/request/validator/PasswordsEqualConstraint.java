package com.common.dto.request.validator;

import com.common.dto.request.validator.impl.PasswordsEqualConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

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
