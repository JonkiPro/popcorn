package com.jonki.popcorn.common.dto.request.validator.impl;

import com.jonki.popcorn.common.dto.request.RegisterRequest;
import com.jonki.popcorn.common.dto.request.validator.PasswordsEqualConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Check whether the passwords are the same. Implementation of the PasswordsEqualConstraint annotation.
 */
public class PasswordsEqualConstraintValidator implements ConstraintValidator<PasswordsEqualConstraint, Object> {

    @Override
    public void initialize(PasswordsEqualConstraint constraintAnnotation) {}

    @Override
    public boolean isValid(Object candidate, ConstraintValidatorContext context) {
        final RegisterRequest registerRequest = (RegisterRequest) candidate;

        return registerRequest.getPassword().equals(registerRequest.getPasswordAgain());
    }
}
