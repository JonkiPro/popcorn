package com.service.app.rest.controller;

import com.service.app.dto.in.ChangeEmailDTO;
import com.service.app.dto.in.ChangePasswordDTO;
import com.service.app.entity.User;
import com.service.app.service.MailService;
import com.service.app.service.UserService;
import com.service.app.utils.EncryptUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping(value = "/settings", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "Settings API", description = "Provides a list of methods that manage settings")
public class SettingsRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private MailService mailService;

    @ApiOperation(value = "It changes the user's email and sends a token to the mail")
    @ApiResponses(value = { @ApiResponse(code = 400, message = "Incorrect data in the form") })
    @PutMapping(value = "/changeEmail")
    public
    HttpEntity<Boolean> changeEmail(
            @ApiParam(value = "Form of e-mail change", required = true) @RequestBody @Valid ChangeEmailDTO changeEmailDTO,
            Principal principal
    ) {
        Optional<User> userOptional = userService.findByUsername(principal.getName());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            user.setEmailChangeToken(EncryptUtils.encrypt(user.getUsername()));
            user.setNewEmail(changeEmailDTO.getEmail());

            mailService.sendMailWithEmailChangeToken(user.getEmail(), user.getEmailChangeToken());

            userService.saveUser(user);
        }

        return ResponseEntity.ok(true);
    }

    @ApiOperation(value = "It changes the user's password")
    @ApiResponses(value = { @ApiResponse(code = 400, message = "Incorrect data in the form") })
    @PutMapping(value = "/changePassword")
    public
    HttpEntity<Boolean> changePassword(
            @ApiParam(value = "Form of password change", required = true) @RequestBody @Valid ChangePasswordDTO changePasswordDTO,
            Principal principal
    ) {
        Optional<User> userOptional = userService.findByUsername(principal.getName());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            user.setPassword(EncryptUtils.encrypt(changePasswordDTO.getNewPassword()));

            userService.saveUser(user);
        }

        return ResponseEntity.ok(true);
    }
}
