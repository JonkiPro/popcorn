package com.service.app.validator.annotation.impl;

import com.service.app.service.UserService;
import com.service.app.validator.annotation.ExistsEmail;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ExistsEmailValidator implements ConstraintValidator<ExistsEmail, String> {

    @Autowired
    private UserService userService;

    private boolean ifExistsReturn;

    @Override
    public void initialize(ExistsEmail constraintAnnotation) {
        this.ifExistsReturn = constraintAnnotation.ifExistsReturn();
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if(ifExistsReturn) {
            return userService.existsByEmail(email);
        } else {
            return !userService.existsByEmail(email);
        }
    }
}
