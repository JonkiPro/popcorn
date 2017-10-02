package com.web.web.view.error;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for a page that has not been found.
 */
@Controller
public class PageNotFoundController implements ErrorController{

    private static final String PATH = "/error";

    /**
     * Creates a redirection.
     *
     * @return The ModelAndView for the homepage
     */
    @RequestMapping(value = PATH)
    public ModelAndView showPageError() {
        return new ModelAndView("redirect:/");
    }

    /**
     * In case the site is not found.
     *
     * @return New address
     */
    @Override
    public String getErrorPath() {
        return PATH;
    }
}
