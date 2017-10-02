package com.web.web.controller;

import com.core.jpa.service.UserSearchService;
import com.core.utils.EncryptUtils;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Methods used to validate data for asynchronous calls in forms on pages.
 */
@RestController
@PreAuthorize("permitAll()")
@RequestMapping(value = "/api/v1.0/users/check")
@Slf4j
@Api(value = "Check UserEntity Data API", description = "Provides a list of methods for checking user data")
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
    @GetMapping("/username")
    public
    HttpEntity<Boolean> checkUsername(
            @ApiParam(value = "The user's name", required = true) @RequestParam final String username,
            @RequestParam(defaultValue = "false", required = false) final boolean negation
    ) {
        log.info("Called with username {}, negation {}", username, negation);

        if(!negation) {
            return ResponseEntity.ok().body(userSearchService.getUserExistsByUsername(username));
        } else {
            return ResponseEntity.ok().body(!userSearchService.getUserExistsByUsername(username));
        }
    }

    @ApiOperation(value = "Check if the e-mail exists")
    @GetMapping("/email")
    public
    HttpEntity<Boolean> checkEmail(
            @ApiParam(value = "The user's e-mail", required = true) @RequestParam final String email,
            @RequestParam(defaultValue = "false", required = false) final boolean negation
    ) {
        log.info("Called with email {}, negation {}", email, negation);

        if(!negation) {
            return ResponseEntity.ok().body(userSearchService.getUserExistsByEmail(email));
        } else {
            return ResponseEntity.ok().body(!userSearchService.getUserExistsByEmail(email));
        }
    }

    @ApiOperation(value = "Check that the user's password is the same")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/password")
    public
    HttpEntity<Boolean> checkPassword(
            @ApiParam(value = "The user's password", required = true) @RequestParam final String password,
            @RequestParam(defaultValue = "false", required = false) final boolean negation,
            final Principal principal
    ) {
        log.info("Called with password {}, negation {}", password, negation);

        if(!negation) {
            return ResponseEntity.ok().body(EncryptUtils.matches(password, userSearchService.getUserPassword(principal.getName())));
        } else {
            return ResponseEntity.ok().body(!(EncryptUtils.matches(password, userSearchService.getUserPassword(principal.getName()))));
        }
    }
}
