package com.service.app.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@PreAuthorize("hasRole('ROLE_ANONYMOUS')")
@RequestMapping("/register")
public class RegisterController {

    /**
     * @return Returns the ModelAndView for the registration page.
     */
    @GetMapping
    public ModelAndView showRegister() {
        return new ModelAndView("register");
    }
}
