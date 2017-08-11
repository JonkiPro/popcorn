package com.service.app.validator.annotation.impl;

import com.service.app.validator.annotation.IsValidEmail;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<IsValidEmail, String> {

    @Override
    public void initialize(IsValidEmail constraintAnnotation) {}

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return org.apache.commons.validator.routines.EmailValidator.getInstance().isValid(email);
    }
}
