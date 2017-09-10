package com.service.app.rest.controller;

import com.service.app.converter.UnidirectionalConverter;
import com.service.app.entity.User;
import com.service.app.exception.TokenNotFoundException;
import com.service.app.rest.request.RegisterDTO;
import com.service.app.security.SecurityRole;
import com.service.app.service.MailService;
import com.service.app.service.UserService;
import com.service.app.utils.EncryptUtils;
import com.service.app.validator.component.RegisterPasswordsValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@PreAuthorize("hasRole('ROLE_ANONYMOUS')")
@RequestMapping(value = "/register")
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
    @PostMapping
    public
    HttpEntity<Boolean> register(
            @RequestBody @Valid RegisterDTO registerDTO
    ) {
        User user = converterRegisterDTOToUser.convert(registerDTO);
        user.setActivationToken(EncryptUtils.encrypt(user.getUsername()));
        user.setAuthorities(SecurityRole.ROLE_USER.toString());

        mailService.sendMailWithActivationToken(user.getEmail(), user.getActivationToken());

        userService.saveUser(user);

        return new ResponseEntity<>(true, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Activate the user with token")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "Token not found") })
    @RequestMapping(value = "/thanks", method = RequestMethod.GET)
    public
    ResponseEntity<?> confirmAccount(
            @RequestParam("token") String token,
            UriComponentsBuilder uriComponentsBuilder
    ) throws URISyntaxException {
        Optional<User> userOptional = userService.findByActivationToken(token);

        if(userOptional.isPresent()) {
            User user = userOptional.get();

            user.setActivationToken(null);
            user.setEnabled(true);

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
