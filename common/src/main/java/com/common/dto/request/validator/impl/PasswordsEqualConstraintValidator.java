package com.common.dto.request.validator.impl;

import com.common.dto.request.RegisterDTO;
import com.common.dto.request.validator.PasswordsEqualConstraint;

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
        final RegisterDTO registerDTO = (RegisterDTO) candidate;

        return registerDTO.getPassword().equals(registerDTO.getPasswordAgain());
    }
}
