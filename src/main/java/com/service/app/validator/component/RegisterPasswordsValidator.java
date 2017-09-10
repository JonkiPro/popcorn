package com.service.app.validator.component;

import com.service.app.rest.request.RegisterDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class RegisterPasswordsValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return RegisterDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RegisterDTO registerDTO = (RegisterDTO) target;

        if(!registerDTO.getPassword().equals(registerDTO.getPasswordAgain())) {
            errors.rejectValue("passwordAgain", "Equals.registerDTO.passwordAgain");
        }

    }
}
