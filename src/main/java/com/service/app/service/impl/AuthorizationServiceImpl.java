package com.service.app.service.impl;

import com.service.app.entity.User;
import com.service.app.service.AuthorizationService;
import com.service.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service("authorizationService")
public class AuthorizationServiceImpl implements AuthorizationService {

    private UserService userService;

    @Autowired
    public AuthorizationServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public User getUser() {
        return userService.findOneByUsername(this.getUsername());
    }

    @Override
    public String getUsername() {
        return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }
}
