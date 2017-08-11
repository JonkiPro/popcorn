package com.service.app.controller.error;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PageNotFoundController implements ErrorController{

    @RequestMapping("/error")
    public ModelAndView showPageError() {
        return new ModelAndView("redirect:/");
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
