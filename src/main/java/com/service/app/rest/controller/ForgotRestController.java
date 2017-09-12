package com.service.app.rest.controller;

import com.service.app.rest.request.ForgotPasswordDTO;
import com.service.app.rest.request.ForgotUsernameDTO;
import com.service.app.entity.User;
import com.service.app.service.MailService;
import com.service.app.service.UserService;
import com.service.app.utils.EncryptUtils;
import com.service.app.utils.RandomUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@PreAuthorize("hasRole('ROLE_ANONYMOUS')")
@Api(value = "Forgot API", description = "Provides a list of methods for reminding username and password")
public class ForgotRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private MailService mailService;

    @ApiOperation(value = "Recover username and send it to the e-mail address")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 400, message = "Incorrect data in the form")
    })
    @PutMapping(value = "/forgotUsername")
    public
    HttpEntity<Boolean> forgotUsername(
            @ApiParam(value = "Username recovery form", required = true) @RequestBody @Valid ForgotUsernameDTO forgotUsernameDTO
    ) {
        Optional<User> userOptional = userService.findByEmail(forgotUsernameDTO.getEmail());

        if(!userOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOptional.get();

        mailService.sendMailWithUsername(user.getEmail(), user.getUsername());

        return ResponseEntity.ok(true);
    }

    @ApiOperation(value = "Generate a new password for the user and send it to the e-mail address")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 400, message = "Incorrect data in the form")
    })
    @PutMapping(value = "/forgotPassword")
    public
    HttpEntity<Boolean> forgotPassword(
            @ApiParam(value = "Password recovery form", required = true) @RequestBody @Valid ForgotPasswordDTO forgotPasswordDTO
    ) {
        Optional<User> userOptional = userService.findByUsernameAndEmail(forgotPasswordDTO.getUsername(), forgotPasswordDTO.getEmail());

        if(!userOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOptional.get();

        String newPassword = RandomUtils.randomPassword();

        user.setPassword(EncryptUtils.encrypt(newPassword));

        mailService.sendMailWithNewPassword(user.getEmail(), newPassword);

        userService.saveUser(user);

        return ResponseEntity.ok(true);
    }
}
