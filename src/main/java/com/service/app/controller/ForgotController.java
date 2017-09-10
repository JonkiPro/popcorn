package com.service.app.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@PreAuthorize("hasRole('ROLE_ANONYMOUS')")
public class ForgotController {

    /**
     * @return Returns the ModelAndView for the page that is responsible for sending the email with the username.
     */
    @GetMapping("/forgotUsername")
    public ModelAndView showFormForgotUsername() {
        return new ModelAndView("forgotUsername");
    }

    /**
     * @return Returns the ModelAndView for the page that is responsible for sending the email with the password.
     */
    @GetMapping("/forgotPassword")
    public ModelAndView showFormForgotPassword() {
        return new ModelAndView("forgotPassword");
    }
}
