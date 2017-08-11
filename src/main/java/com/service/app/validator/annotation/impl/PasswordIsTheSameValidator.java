package com.service.app.validator.annotation.impl;

import com.service.app.service.UserService;
import com.service.app.utils.EncryptUtils;
import com.service.app.validator.annotation.PasswordIsTheSame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordIsTheSameValidator implements ConstraintValidator<PasswordIsTheSame, String> {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(PasswordIsTheSame constraintAnnotation) {}

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        return EncryptUtils.matches(password, userService.findOneByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getPassword());
    }
}
