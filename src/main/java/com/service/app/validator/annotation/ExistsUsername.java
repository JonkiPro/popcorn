package com.service.app.validator.annotation;

import com.service.app.validator.annotation.impl.ExistsUsernameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExistsUsernameValidator.class)
@Target( {ElementType.FIELD} )
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistsUsername {

    boolean ifExistsReturn() default true;

    String message() default "Username exists in database.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
