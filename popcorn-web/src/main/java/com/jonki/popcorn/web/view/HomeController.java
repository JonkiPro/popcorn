package com.jonki.popcorn.web.view;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@PreAuthorize("permitAll()")
@RequestMapping(value = "/")
public class HomeController {

    /**
     * @return The ModelAndView for the homepage
     */
    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("index");
    }
}
