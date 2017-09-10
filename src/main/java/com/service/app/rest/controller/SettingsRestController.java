package com.service.app.rest.controller;

import com.service.app.exception.TokenNotFoundException;
import com.service.app.rest.request.ChangeEmailDTO;
import com.service.app.rest.request.ChangePasswordDTO;
import com.service.app.entity.User;
import com.service.app.service.MailService;
import com.service.app.service.UserService;
import com.service.app.utils.EncryptUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping(value = "/settings", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @GetMapping("/changeEmail/thanks")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "Token not found") })
    public
    ResponseEntity<?> emailChangeToken(
            @RequestParam("token") String token,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        Optional<User> userOptional = userService.findByEmailChangeToken(token);

        if(userOptional.isPresent()) {
            User user = userOptional.get();

            user.setEmail(user.getNewEmail());
            user.setEmailChangeToken(null);
            user.setNewEmail(null);

            userService.saveUser(user);
        } else {
            throw new TokenNotFoundException();
        }

        UriComponents uriComponents = uriComponentsBuilder.path("/").build();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponents.toUri());

        return new ResponseEntity<>(httpHeaders, HttpStatus.MOVED_PERMANENTLY);
    }
}
