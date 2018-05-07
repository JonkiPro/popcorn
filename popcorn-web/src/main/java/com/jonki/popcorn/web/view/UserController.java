package com.jonki.popcorn.web.view;

import com.jonki.popcorn.core.service.UserSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
@PreAuthorize("permitAll()")
public class UserController {

    private final UserSearchService userSearchService;

    /**
     * Constructor.
     *
     * @param userSearchService The user search service to use
     */
    @Autowired
    public UserController(
            final UserSearchService userSearchService
    ) {
        this.userSearchService = userSearchService;
    }

    /**
     * @return The ModelAndView for the list of users
     */
    @GetMapping(value = "/users")
    public ModelAndView users() {
        return new ModelAndView("users");
    }

    /**
     * @param username The user's name
     * @param modelMap {@link ModelMap}
     * @return The ModelAndView for the user profile
     */
    @GetMapping(value = "/profile/{username}")
    public ModelAndView getProfile(
            @PathVariable final String username,
            final ModelMap modelMap
    ) {
        if(!this.userSearchService.existsUserByUsername(username.trim())) {
            return new ModelAndView("redirect:/users");
        }

        modelMap.addAttribute("username", username);

        return new ModelAndView("profile", modelMap);
    }
}
