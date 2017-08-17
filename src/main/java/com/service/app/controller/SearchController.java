package com.service.app.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@PreAuthorize("permitAll()")
public class SearchController {

    /**
     * @param type Type of data sought.
     * @param q Search for a phrase.
     * @param page Page number.
     * @param pageSize Number of items per page.
     * @param modelMap {@link ModelMap}
     * @return Returns the ModelAndView for a list of users (or a list of movies( in the future )).
     */
    @GetMapping("/search")
    public ModelAndView search(
            @RequestParam String type,
            @RequestParam String q,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "1") int pageSize,
            ModelMap modelMap
    ) {
        modelMap.addAttribute("type", type);
        modelMap.addAttribute("q", q);
        modelMap.addAttribute("page", page);
        modelMap.addAttribute("pageSize", pageSize);

        return new ModelAndView("users", modelMap);
    }
}
