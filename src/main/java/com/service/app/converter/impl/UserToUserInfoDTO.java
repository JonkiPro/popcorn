package com.service.app.converter.impl;

import com.service.app.converter.UnidirectionalConverter;
import com.service.app.rest.response.UserInfoDTO;
import com.service.app.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserToUserInfoDTO implements UnidirectionalConverter<User, UserInfoDTO> {

    @Override
    public UserInfoDTO convert(User user) {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setUsername(user.getUsername());

        return userInfoDTO;
    }
}
