package com.service.app.controller;

import com.service.app.entity.User;
import com.service.app.exception.EmailChangeTokenNotFoundException;
import com.service.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("/settings")
public class SettingsController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ModelAndView settings() {
        return new ModelAndView("settings");
    }

    @GetMapping("/changeEmail/thanks")
    public ModelAndView emailChangeToken(
            @RequestParam("token") String token
    ) {
        Optional<User> userOptional = userService.findByEmailChangeToken(token);

        if(userOptional.isPresent()) {
            User user = userOptional.get();

            user.setEmail(user.getNewEmail());
            user.setEmailChangeToken(null);
            user.setNewEmail(null);

            userService.saveUser(user);
        } else {
            throw new EmailChangeTokenNotFoundException();
        }

        return new ModelAndView("redirect:/signIn");
    }
}
