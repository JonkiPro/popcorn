package com.service.app.rest.controller;

import com.service.app.service.UserService;
import com.service.app.utils.EncryptUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@PreAuthorize("permitAll()")
@RequestMapping(value = "/checkUserData")
@Api(value = "Check User Data API", description = "Provides a list of methods for checking user data")
public class CheckUserDataRestController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Return false if the user by username exists")
    @GetMapping("/checkUsernameAtRegistering")
    public
    HttpEntity<Boolean> checkUsernameAtRegistering(
            @ApiParam(value = "The user's name", required = true) @RequestParam String username
    ) {
        return ResponseEntity.ok().body(!userService.existsByUsername(username));
    }

    @ApiOperation(value = "Return false if the user by e-mail exists")
    @GetMapping("/checkEmailAtRegistering")
    public
    HttpEntity<Boolean> checkEmailAtRegistering(
            @ApiParam(value = "The user's e-mail", required = true) @RequestParam String email
    ) {
        return ResponseEntity.ok().body(!userService.existsByEmail(email));
    }

    @ApiOperation(value = "Return true if the user by username exists")
    @GetMapping("/checkUsername")
    public
    HttpEntity<Boolean> checkUsername(
            @ApiParam(value = "The user's name", required = true) @RequestParam String username
    ) {
        return ResponseEntity.ok().body(userService.existsByUsername(username));
    }

    @ApiOperation(value = "Return true if the user by e-mail exists")
    @GetMapping("/checkEmail")
    public
    HttpEntity<Boolean> checkEmail(
            @ApiParam(value = "The user's e-mail", required = true) @RequestParam String email
    ) {
        return ResponseEntity.ok().body(userService.existsByEmail(email));
    }

    @ApiOperation(value = "Return true if the given password is the same as the database")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/checkPassword")
    public
    HttpEntity<Boolean> checkPassword(
            @ApiParam(value = "The user's password", required = true) @RequestParam String password,
            Principal principal
    ) {
        return ResponseEntity.ok().body(EncryptUtils.matches(password, userService.findOneByUsername(principal.getName()).getPassword()));
    }

    @ApiOperation(value = "Return false if the given password is the same as the database")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/checkPasswordIsTheSame")
    public
    HttpEntity<Boolean> checkPasswordIsTheSame(
            @ApiParam(value = "The user's password", required = true) @RequestParam String password,
            Principal principal
    ) {
        return ResponseEntity.ok().body(!(EncryptUtils.matches(password, userService.findOneByUsername(principal.getName()).getPassword())));
    }
}
