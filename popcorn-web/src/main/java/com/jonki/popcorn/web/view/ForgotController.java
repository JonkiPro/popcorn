package com.jonki.popcorn.web.view;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@PreAuthorize("hasRole('ROLE_ANONYMOUS')")
public class ForgotController {

    /**
     * @return The ModelAndView for the page that is responsible for recovering the username
     */
    @GetMapping(value = "/forgotUsername")
    public ModelAndView showFormForgotUsername() {
        return new ModelAndView("forgotUsername");
    }

    /**
     * @return The ModelAndView for the page that is responsible for resetting the user password
     */
    @GetMapping(value = "/forgotPassword")
    public ModelAndView showFormForgotPassword() {
        return new ModelAndView("forgotPassword");
    }
}
