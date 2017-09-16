package com.service.app.rest.controller;

import com.service.app.service.UserService;
import com.service.app.utils.EncryptUtils;
import io.swagger.annotations.*;
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
@Api(value = "Check User Data API", description = "Provides a list of methods for checking user data")
public class CheckUserDataRestController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Check if the username exists")
    @GetMapping("/username")
    public
    HttpEntity<Boolean> checkUsername(
            @ApiParam(value = "The user's name", required = true) @RequestParam String username,
            @RequestParam(defaultValue = "false", required = false) boolean negation
    ) {
        if(!negation) {
            return ResponseEntity.ok().body(userService.existsByUsername(username));
        } else {
            return ResponseEntity.ok().body(!userService.existsByUsername(username));
        }
    }

    @ApiOperation(value = "Check if the e-mail exists")
    @GetMapping("/email")
    public
    HttpEntity<Boolean> checkEmail(
            @ApiParam(value = "The user's e-mail", required = true) @RequestParam String email,
            @RequestParam(defaultValue = "false", required = false) boolean negation
    ) {
        if(!negation) {
            return ResponseEntity.ok().body(userService.existsByEmail(email));
        } else {
            return ResponseEntity.ok().body(!userService.existsByEmail(email));
        }
    }

    @ApiOperation(value = "Check that the user's password is the same")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/password")
    public
    HttpEntity<Boolean> checkPassword(
            @ApiParam(value = "The user's password", required = true) @RequestParam String password,
            @RequestParam(defaultValue = "false", required = false) boolean negation,
            Principal principal
    ) {
        if(!negation) {
            return ResponseEntity.ok().body(EncryptUtils.matches(password, userService.findOneByUsername(principal.getName()).getPassword()));
        } else {
            return ResponseEntity.ok().body(!(EncryptUtils.matches(password, userService.findOneByUsername(principal.getName()).getPassword())));
        }
    }
}
