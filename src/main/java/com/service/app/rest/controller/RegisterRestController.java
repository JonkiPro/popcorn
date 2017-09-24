package com.service.app.rest.controller;

import com.service.app.converter.UnidirectionalConverter;
import com.service.app.entity.User;
import com.service.app.rest.request.RegisterDTO;
import com.service.app.security.role.SecurityRole;
import com.service.app.service.MailService;
import com.service.app.service.UserService;
import com.service.app.utils.RandomUtils;
import com.service.app.validator.component.RegisterPasswordsValidator;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@PreAuthorize("hasRole('ROLE_ANONYMOUS')")
@RequestMapping(value = "/api/v1.0/register")
@Api(value = "Register API", description = "Provides a list of methods for registration")
public class RegisterRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private MailService mailService;
    @Autowired
    private RegisterPasswordsValidator registerPasswordsValidator;
    @Autowired
    private UnidirectionalConverter<RegisterDTO, User> converterRegisterDTOToUser;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.addValidators(registerPasswordsValidator);
    }

    @ApiOperation(value = "Register the user", code = 201)
    @ApiResponses(value = { @ApiResponse(code = 400, message = "Incorrect data in the form") })
    @PostMapping
    public
    HttpEntity<Boolean> register(
            @ApiParam(value = "Registration form", required = true) @RequestBody @Valid RegisterDTO registerDTO,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        User user = converterRegisterDTOToUser.convert(registerDTO);
        user.setActivationToken(RandomUtils.randomToken());
        user.setAuthorities(SecurityRole.ROLE_USER);

        mailService.sendMailWithActivationToken(user.getEmail(), user.getActivationToken());

        userService.saveUser(user);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("/register/successfully").build().toUri());

        return new ResponseEntity<>(true, httpHeaders, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Activate the user with token")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "Token not found") })
    @PutMapping(value = "/token/{token}")
    public
    HttpEntity<Boolean> confirmAccount(
            @ApiParam(value = "Account activation token", required = true) @PathVariable String token
    ) {
        Optional<User> userOptional = userService.findByActivationToken(token);

        return userOptional
                .map(user -> {
                    user.setActivationToken(null);
                    user.setEnabled(true);

                    userService.saveUser(user);

                    return ResponseEntity.ok(true);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
