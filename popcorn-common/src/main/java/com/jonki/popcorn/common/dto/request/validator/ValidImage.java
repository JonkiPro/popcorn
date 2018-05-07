package com.jonki.popcorn.common.dto.request.validator;

import com.jonki.popcorn.common.dto.request.validator.impl.ImageValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Validate the image file.
 */
@Documented
@Constraint(validatedBy = ImageValidator.class)
@Target( {ElementType.FIELD} )
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidImage {

    String message() default "Invalid file type.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}