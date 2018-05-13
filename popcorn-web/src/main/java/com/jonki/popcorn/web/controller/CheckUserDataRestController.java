package com.jonki.popcorn.web.controller;

import com.jonki.popcorn.core.service.UserSearchService;
import com.jonki.popcorn.core.util.EncryptUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * Methods used to validate data for asynchronous calls in forms on pages.
 */
@RestController
@PreAuthorize("permitAll()")
@RequestMapping(value = "/api/v1.0/users/check")
@Slf4j
@Api(value = "Check User Data API", description = "Provides a list of methods for checking user data")
public class CheckUserDataRestController {

    private final UserSearchService userSearchService;

    /**
     * Constructor.
     *
     * @param userSearchService The user search service to use
     */
    @Autowired
    public CheckUserDataRestController(
            final UserSearchService userSearchService
    ) {
        this.userSearchService = userSearchService;
    }

    @ApiOperation(value = "Check if the username exists")
    @GetMapping(value = "/username", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public
    boolean checkUsername(
            @ApiParam(value = "The user's name", required = true)
            @RequestParam final String username,
            @ApiParam(value = "Negation of the returned value")
            @RequestParam(defaultValue = "false", required = false) final boolean negation
    ) {
        log.info("Called with username {}, negation {}", username, negation);

        if(!negation) {
            return userSearchService.existsUserByUsername(username);
        } else {
            return !userSearchService.existsUserByUsername(username);
        }
    }

    @ApiOperation(value = "Check if the e-mail exists")
    @GetMapping(value = "/email", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public
    boolean checkEmail(
            @ApiParam(value = "The user's e-mail", required = true)
            @RequestParam final String email,
            @ApiParam(value = "Negation of the returned value")
            @RequestParam(defaultValue = "false", required = false) final boolean negation
    ) {
        log.info("Called with email {}, negation {}", email, negation);

        if(!negation) {
            return userSearchService.existsUserByEmail(email);
        } else {
            return !userSearchService.existsUserByEmail(email);
        }
    }

    @ApiOperation(value = "Check that the user's password is the same")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value = "/password", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public
    boolean checkPassword(
            @ApiParam(value = "The user's password", required = true)
            @RequestParam final String password,
            @ApiParam(value = "Negation of the returned value")
            @RequestParam(defaultValue = "false", required = false) final boolean negation,
            final Principal principal
    ) {
        log.info("Called with password {}, negation {}", password, negation);

        if(!negation) {
            return EncryptUtils.matches(password, userSearchService.getUserPassword(principal.getName()));
        } else {
            return !(EncryptUtils.matches(password, userSearchService.getUserPassword(principal.getName())));
        }
    }
}
