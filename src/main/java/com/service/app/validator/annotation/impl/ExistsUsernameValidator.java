package com.service.app.validator.annotation.impl;

import com.service.app.service.UserService;
import com.service.app.validator.annotation.ExistsUsername;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ExistsUsernameValidator implements ConstraintValidator<ExistsUsername, String> {

    @Autowired
    private UserService userService;

    private boolean ifExistsReturn;

    @Override
    public void initialize(ExistsUsername constraintAnnotation) {
        this.ifExistsReturn = constraintAnnotation.ifExistsReturn();
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if(ifExistsReturn) {
            return userService.existsByUsername(username);
        } else {
            return !userService.existsByUsername(username);
        }
    }
}
