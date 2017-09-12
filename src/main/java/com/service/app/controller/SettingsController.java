package com.service.app.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

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

    /**
     * This method activates the e-mail change using a token.
     * @param token E-mail change activation token.
     * @param uriComponentsBuilder {@link UriComponentsBuilder}
     * @return Returns the ModelAndView for status.
     */
    @PreAuthorize("permitAll()")
    @GetMapping(value = "changeEmail/thanks")
    public
    ModelAndView confirmEmail(
            @RequestParam String token,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Object> entity = new HttpEntity<>(new HttpHeaders());

        UriComponents uriComponents
                = uriComponentsBuilder.path("/settings/changeEmail/token/{token}").buildAndExpand(token);

        ResponseEntity<Boolean> response;

        try {
            response = restTemplate
                    .exchange(uriComponents.toUri(),
                              HttpMethod.PUT,
                              entity,
                              Boolean.class);
        } catch (HttpClientErrorException e) /* IF 404 */ {
            return new ModelAndView("tokenNotFound");
        }

        /* IF 200 */
        return new ModelAndView("redirect:/signIn");
    }
}
