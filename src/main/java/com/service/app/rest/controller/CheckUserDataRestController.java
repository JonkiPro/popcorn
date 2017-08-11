package com.service.app.rest.controller;

import com.service.app.service.UserService;
import com.service.app.utils.EncryptUtils;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiQueryParam;
import org.jsondoc.core.annotation.ApiResponseObject;
import org.jsondoc.core.pojo.ApiStage;
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
@Api(name = "Check User Data API", description = "Provides a list of methods for checking user data", group = "User", stage = ApiStage.BETA)
public class CheckUserDataRestController {

    @Autowired
    private UserService userService;

    @ApiMethod(description = "Return false if the user by username exists")
    @GetMapping("/checkUsernameAtRegistering")
    public @ApiResponseObject
    HttpEntity<Boolean> checkUsernameAtRegistering(
            @ApiQueryParam(description = "The user's name") @RequestParam String username
    ) {
        return ResponseEntity.ok().body(!userService.existsByUsername(username));
    }

    @ApiMethod(description = "Return false if the user by e-mail exists")
    @GetMapping("/checkEmailAtRegistering")
    public @ApiResponseObject
    HttpEntity<Boolean> checkEmailAtRegistering(
            @ApiQueryParam(description = "The user's e-mail") @RequestParam String email
    ) {
        return ResponseEntity.ok().body(!userService.existsByEmail(email));
    }

    @ApiMethod(description = "Return true if the user by username exists")
    @GetMapping("/checkUsername")
    public @ApiResponseObject
    HttpEntity<Boolean> checkUsername(
            @ApiQueryParam(description = "The user's name") @RequestParam String username
    ) {
        return ResponseEntity.ok().body(userService.existsByUsername(username));
    }

    @ApiMethod(description = "Return true if the user by e-mail exists")
    @GetMapping("/checkEmail")
    public @ApiResponseObject
    HttpEntity<Boolean> checkEmail(
            @ApiQueryParam(description = "The user's e-mail") @RequestParam String email
    ) {
        return ResponseEntity.ok().body(userService.existsByEmail(email));
    }

    @ApiMethod(description = "Return true if the given password is the same as the database")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/checkPassword")
    public @ApiResponseObject
    HttpEntity<Boolean> checkPassword(
            @ApiQueryParam(description = "The user's password") @RequestParam String password,
            Principal principal
    ) {
        return ResponseEntity.ok().body(EncryptUtils.matches(password, userService.findOneByUsername(principal.getName()).getPassword()));
    }

    @ApiMethod(description = "Return false if the given password is the same as the database")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/checkPasswordIsTheSame")
    public @ApiResponseObject
    HttpEntity<Boolean> checkPasswordIsTheSame(
            @ApiQueryParam(description = "The user's password") @RequestParam String password,
            Principal principal
    ) {
        return ResponseEntity.ok().body(!(EncryptUtils.matches(password, userService.findOneByUsername(principal.getName()).getPassword())));
    }
}
