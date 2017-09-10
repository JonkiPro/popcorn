package com.service.app.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("/settings")
public class SettingsController {

    /**
     * @return Returns the ModelAndView for the account settings page.
     */
    @GetMapping
    public ModelAndView settings() {
        return new ModelAndView("settings");
    }
}
