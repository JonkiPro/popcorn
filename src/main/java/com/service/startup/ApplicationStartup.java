package com.service.startup;

import com.service.app.entity.User;
import com.service.app.security.role.SecurityRole;
import com.service.app.service.UserService;
import com.service.app.utils.EncryptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private UserService userService;

    // Administrator data
    private final String USERNAME = "JonkiPro";
    private final String PASSWORD = "VeryStrongPassword1";
    private final String EMAIL = "someemail@someemail.com";

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if(!userService.findByUsername(USERNAME).isPresent()) { userService.saveUser(initAdmin()); }
    }

    /**
     * This method initializes the application administrator.
     * @return user (administrator)
     */
    public User initAdmin() {
        User user = new User();
        user.setUsername(USERNAME);
        user.setPassword(EncryptUtils.encrypt(PASSWORD));
        user.setEmail(EMAIL);
        user.setEnabled(true);
        user.setAuthorities(EnumSet.of(SecurityRole.ROLE_USER, SecurityRole.ROLE_ADMIN));

        return user;
    }
}
