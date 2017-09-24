package com.service.app.rest.controller;

import com.service.app.rest.request.ChangeEmailDTO;
import com.service.app.rest.request.ChangePasswordDTO;
import com.service.app.entity.User;
import com.service.app.service.MailService;
import com.service.app.service.UserService;
import com.service.app.utils.EncryptUtils;
import com.service.app.utils.RandomUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping(value = "/api/v1.0/users/account/update", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "Settings API", description = "Provides a list of methods that manage settings")
public class SettingsRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private MailService mailService;

    @ApiOperation(value = "It changes the user's email and sends a token to the mail")
    @ApiResponses(value = { @ApiResponse(code = 400, message = "Incorrect data in the form") })
    @PutMapping(value = "/email")
    public
    HttpEntity<Boolean> changeEmail(
            @ApiParam(value = "Form of e-mail change", required = true) @RequestBody @Valid ChangeEmailDTO changeEmailDTO,
            Principal principal
    ) {
        User user = userService.findOneByUsername(principal.getName());

        user.setEmailChangeToken(RandomUtils.randomToken());
        user.setNewEmail(changeEmailDTO.getEmail());

        mailService.sendMailWithEmailChangeToken(user.getEmail(), user.getEmailChangeToken());

        userService.saveUser(user);

        return ResponseEntity.ok(true);
    }

    @ApiOperation(value = "It changes the user's password")
    @ApiResponses(value = { @ApiResponse(code = 400, message = "Incorrect data in the form") })
    @PutMapping(value = "/password")
    public
    HttpEntity<Boolean> changePassword(
            @ApiParam(value = "Form of password change", required = true) @RequestBody @Valid ChangePasswordDTO changePasswordDTO,
            Principal principal
    ) {
        User user = userService.findOneByUsername(principal.getName());

        user.setPassword(EncryptUtils.encrypt(changePasswordDTO.getNewPassword()));

        userService.saveUser(user);

        return ResponseEntity.ok(true);
    }

    @ApiOperation(value = "Activates e-mail change with token")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "Token not found") })
    @PreAuthorize("permitAll()")
    @PutMapping(value = "/email/token/{token}")
    public
    HttpEntity<Boolean> emailChangeToken(
            @ApiParam(value = "E-mail change token", required = true) @PathVariable String token
    ) {
        Optional<User> userOptional = userService.findByEmailChangeToken(token);

        return userOptional
                .map(user -> {
                    user.setEmail(user.getNewEmail());
                    user.setEmailChangeToken(null);
                    user.setNewEmail(null);

                    userService.saveUser(user);

                    return ResponseEntity.ok(true);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
