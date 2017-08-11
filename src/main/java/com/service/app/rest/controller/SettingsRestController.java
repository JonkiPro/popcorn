package com.service.app.rest.controller;

import com.service.app.dto.in.ChangeEmailDTO;
import com.service.app.dto.in.ChangePasswordDTO;
import com.service.app.entity.User;
import com.service.app.service.MailService;
import com.service.app.service.UserService;
import com.service.app.utils.EncryptUtils;
import org.jsondoc.core.annotation.*;
import org.jsondoc.core.pojo.ApiStage;
import org.jsondoc.core.pojo.ApiVerb;
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
@Api(name = "Settings API", description = "Provides a list of methods that manage settings", group = "Settings", stage = ApiStage.BETA)
public class SettingsRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private MailService mailService;

    @ApiMethod(description = "It changes the user's email and sends a token to the mail", verb = ApiVerb.PUT)
    @ApiErrors(apierrors = { @ApiError(code = "400", description = "Incorrect data in the form") })
    @PutMapping(value = "/changeEmail")
    public @ApiResponseObject
    HttpEntity<Boolean> changeEmail(
            @ApiBodyObject @RequestBody @Valid ChangeEmailDTO changeEmailDTO,
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

    @ApiMethod(description = "It changes the user's password", verb = ApiVerb.PUT)
    @ApiErrors(apierrors = { @ApiError(code = "400", description = "Incorrect data in the form") })
    @PutMapping(value = "/changePassword")
    public @ApiResponseObject
    HttpEntity<Boolean> changePassword(
            @ApiBodyObject @RequestBody @Valid ChangePasswordDTO changePasswordDTO,
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
