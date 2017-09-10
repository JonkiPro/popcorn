package com.service.app.validator.component;

import com.service.app.rest.request.ChangePasswordDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ChangePasswordsValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ChangePasswordDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ChangePasswordDTO changePasswordDTO = (ChangePasswordDTO) target;

        if(!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getNewPasswordAgain())) {
            errors.rejectValue("newPasswordAgain", "Equals.changePasswordDTO.newPasswordAgain");
        }

    }
}
