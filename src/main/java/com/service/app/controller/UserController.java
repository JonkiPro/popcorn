package com.service.app.controller;

import com.service.app.exception.ProfileNotFoundException;
import com.service.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
@PreAuthorize("permitAll()")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * @return Returns the ModelAndView for the list of users.
     */
    @GetMapping("/users")
    public ModelAndView users() {
        return new ModelAndView("users");
    }

    /**
     * @param username The user's name.
     * @param modelMap {@link ModelMap}
     * @return Returns the ModelAndView for the user profile.
     */
    @GetMapping("/profile/{username}")
    public ModelAndView getProfile(
            @PathVariable String username,
            ModelMap modelMap
    ) {
        this.validUserExistence(username);

        modelMap.addAttribute("username", username);

        return new ModelAndView("profile", modelMap);
    }

    /**
     * This method checks to see if the user exists.
     * @param username The user's name.
     */
    private void validUserExistence(String username) {
        userService.findAll().stream()
                .filter(v -> username.equals(v.getUsername()))
                .findAny()
                .orElseThrow(ProfileNotFoundException::new);
    }
}
