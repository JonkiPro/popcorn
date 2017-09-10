package com.service.app.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@PreAuthorize("permitAll()")
@RequestMapping("/")
public class HomeController {

    /**
     * @return Returns the ModelAndView for the homepage.
     */
    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("index");
    }
}
