package com.common.dto.request.validator.impl;

import com.common.dto.request.validator.ValidEmail;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validate e-mail address syntax. Implementation of the ValidEmail annotation.
 */
public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    @Override
    public void initialize(ValidEmail constraintAnnotation) {}

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return org.apache.commons.validator.routines.EmailValidator.getInstance().isValid(email);
    }
}
