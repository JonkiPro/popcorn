package com.jonki.popcorn.web.controller;

import com.jonki.popcorn.common.dto.error.ValidationErrorDTO;
import com.jonki.popcorn.common.dto.request.RegisterRequest;
import com.jonki.popcorn.common.exception.FormBadRequestException;
import com.jonki.popcorn.core.properties.BundleProperties;
import com.jonki.popcorn.core.service.UserPersistenceService;
import com.jonki.popcorn.core.service.UserSearchService;
import com.jonki.popcorn.web.recaptcha.ReCaptchaProperties;
import com.jonki.popcorn.web.recaptcha.ReCaptchaResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.ResourceBundle;

@RestController
@PreAuthorize("hasRole('ROLE_ANONYMOUS')")
@RequestMapping(value = "/api/v1.0/register")
@Slf4j
@Api(value = "Register API", description = "Provides a list of methods for registration")
public class RegisterRestController {

    private final ResourceBundle bundle = ResourceBundle.getBundle(BundleProperties.VALIDATION_MESSAGES.getSource(), LocaleContextHolder.getLocale());
    private final UserPersistenceService userPersistenceService;
    private final UserSearchService userSearchService;
    private final ReCaptchaProperties reCaptchaProperties;

    /**
     * Constructor - initialize all required dependencies.
     *
     * @param userPersistenceService The user persistence service to use
     * @param userSearchService The user search service to use
     * @param reCaptchaProperties The reCaptcha properties to use
     */
    @Autowired
    public RegisterRestController(
            final UserPersistenceService userPersistenceService,
            final UserSearchService userSearchService,
            final ReCaptchaProperties reCaptchaProperties
    ) {
        this.userPersistenceService = userPersistenceService;
        this.userSearchService = userSearchService;
        this.reCaptchaProperties = reCaptchaProperties;
    }

    @ApiOperation(value = "Register the user")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Incorrect data in the form or the username or e-mail exists"),
            @ApiResponse(code = 409, message = "The username or e-mail exists")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public
    ResponseEntity<Void> register(
            @ApiParam(value = "Registration form", required = true)
            @RequestBody @Valid final RegisterRequest registerRequest,
            final UriComponentsBuilder uriComponentsBuilder
    ) {
        log.info("Called with {}", registerRequest);

        this.validReCaptcha(registerRequest.getReCaptcha());
        this.validRegisterDTO(registerRequest);

        this.userPersistenceService.createUser(registerRequest);

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("/register/successfully").build().toUri());

        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Activate the user with token")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "Token not found") })
    @PutMapping(value = "/token/{token}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void confirmAccount(
            @ApiParam(value = "Account activation token", required = true)
            @PathVariable("token") final String token
    ) {
        log.info("Called with token {}", token);

        this.userPersistenceService.activationUser(token);
    }


    /**
     * Check if there is a user with the given name and email.
     *
     * @param registerRequest RegisterRequest object
     * @throws FormBadRequestException if any data exists
     */
    private void validRegisterDTO(final RegisterRequest registerRequest) {
        final ValidationErrorDTO validationErrorDTO = new ValidationErrorDTO();

        if(this.userSearchService.existsUserByUsername(registerRequest.getUsername())) {
            validationErrorDTO.addFieldError("username", this.bundle.getString("validation.existsUsername"));
        }
        if(this.userSearchService.existsUserByEmail(registerRequest.getEmail())) {
            validationErrorDTO.addFieldError("email", this.bundle.getString("validation.existsEmail"));
        }

        if(!validationErrorDTO.getFieldErrors().isEmpty()) {
            throw new FormBadRequestException(validationErrorDTO);
        }
    }

    /**
     * Check ReCaptcha.
     *
     * @param reCaptcha ReCaptcha
     * @throws FormBadRequestException if the reCaptcha is incorrect
     */
    private void validReCaptcha(final String reCaptcha) {
        final ValidationErrorDTO validationErrorDTO = new ValidationErrorDTO();

        if (reCaptcha == null || reCaptcha.equals("")) {
            validationErrorDTO.addFieldError("reCaptcha", this.bundle.getString("validation.notNull.reCaptcha"));
            throw new FormBadRequestException(validationErrorDTO);
        }

        try {
            final RestTemplate restTemplate = new RestTemplate();
            final HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            final UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(reCaptchaProperties.getApiUrl()).queryParam("secret", reCaptchaProperties.getSecretKey())
                    .queryParam("response", reCaptcha);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            final ResponseEntity<ReCaptchaResponse> response = restTemplate.exchange(builder
                            .build().encode().toUri(), HttpMethod.GET, entity,
                    ReCaptchaResponse.class);

            if(!response.getStatusCode().toString().equals("200")) {
                validationErrorDTO.addFieldError("reCaptcha", bundle.getString("validation.incorrect.reCaptcha"));
                throw new FormBadRequestException(validationErrorDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
