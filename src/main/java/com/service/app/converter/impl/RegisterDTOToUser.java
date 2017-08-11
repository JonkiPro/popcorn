package com.service.app.converter.impl;

import com.service.app.converter.UnidirectionalConverter;
import com.service.app.dto.in.RegisterDTO;
import com.service.app.utils.EncryptUtils;
import com.service.app.entity.User;
import org.springframework.stereotype.Component;

@Component
public class RegisterDTOToUser implements UnidirectionalConverter<RegisterDTO, User> {

    @Override
    public User convert(RegisterDTO registerDTO) {
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(EncryptUtils.encrypt(registerDTO.getPassword()));

        return user;
    }
}
