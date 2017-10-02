package com.common.dto.request.validator.impl;

import com.common.dto.request.validator.IsValidEmail;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validate e-mail address syntax. Implementation of the IsValidEmail annotation.
 */
public class EmailValidator implements ConstraintValidator<IsValidEmail, String> {

    @Override
    public void initialize(IsValidEmail constraintAnnotation) {}

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return org.apache.commons.validator.routines.EmailValidator.getInstance().isValid(email);
    }
}
