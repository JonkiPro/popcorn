package com.web.web.view;

import com.web.web.utils.SSLContextHelper;
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
@RequestMapping(value = "/register")
public class RegisterController {

    /**
     * @return The ModelAndView for the registration page
     */
    @GetMapping
    public ModelAndView showRegister() {
        return new ModelAndView("register");
    }

    /**
     * This method activates the account with a token.
     *
     * @param token Account activation token
     * @param uriComponentsBuilder {@link UriComponentsBuilder}
     * @return The ModelAndView for sign in
     */
    @GetMapping(value = "/thanks")
    public
    ModelAndView confirmAccount(
            @RequestParam final String token,
            final UriComponentsBuilder uriComponentsBuilder
    ) {
        SSLContextHelper.disable();

        final RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<Object> entity = new HttpEntity<>(new HttpHeaders());

        final UriComponents uriComponents
                = uriComponentsBuilder.path("/api/v1.0/register/token/{token}").buildAndExpand(token);

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

    /**
     * @return The ModelAndView after successful account activation
     */
    @GetMapping(value = "/successfully")
    public ModelAndView registerSuccessfully() {
        return new ModelAndView("registerSuccessfully");
    }
}
