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
@PreAuthorize("hasRole('ROLE_ANONYMOUS')")
@RequestMapping("/register")
public class RegisterController {

    /**
     * @return Returns the ModelAndView for the registration page.
     */
    @GetMapping
    public ModelAndView showRegister() {
        return new ModelAndView("register");
    }

    /**
     * This method activates the account with a token.
     * @param token Account activation token.
     * @param uriComponentsBuilder {@link UriComponentsBuilder}
     * @return Returns the ModelAndView for status.
     */
    @GetMapping(value = "/thanks")
    public
    ModelAndView confirmAccount(
            @RequestParam String token,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Object> entity = new HttpEntity<>(new HttpHeaders());

        UriComponents uriComponents
                = uriComponentsBuilder.path("/register/token/{token}").buildAndExpand(token);

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
