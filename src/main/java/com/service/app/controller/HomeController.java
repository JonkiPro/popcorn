package com.service.app.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@PreAuthorize("permitAll()")
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    @GetMapping("jsondoc-ui")
    public ModelAndView json() {
        return new ModelAndView("jsondoc/jsondoc-ui");
    }
}
