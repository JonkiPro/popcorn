package com.service.app.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@PreAuthorize("hasRole('ROLE_ANONYMOUS')")
@RequestMapping("/signIn")
public class SignInController {

    /**
     * @return Returns the ModelAndView for the login page.
     */
    @GetMapping
    public ModelAndView signIn() {
        return new ModelAndView("signIn");
    }
}
