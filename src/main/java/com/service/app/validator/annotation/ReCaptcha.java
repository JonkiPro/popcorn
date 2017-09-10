package com.service.app.validator.annotation;

import com.service.app.validator.annotation.impl.ReCaptchaValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ReCaptchaValidator.class)
@Target( {ElementType.FIELD} )
@Retention(RetentionPolicy.RUNTIME)
public @interface ReCaptcha {

    String message() default "Incorrect reCaptcha.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
