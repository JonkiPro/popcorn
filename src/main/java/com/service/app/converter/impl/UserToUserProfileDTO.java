package com.service.app.converter.impl;

import com.service.app.converter.UnidirectionalConverter;
import com.service.app.rest.response.UserProfileDTO;
import com.service.app.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserToUserProfileDTO implements UnidirectionalConverter<User, UserProfileDTO> {

    @Override
    public UserProfileDTO convert(User user) {
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setId(user.getId());
        userProfileDTO.setUsername(user.getUsername());

        return userProfileDTO;
    }
}
