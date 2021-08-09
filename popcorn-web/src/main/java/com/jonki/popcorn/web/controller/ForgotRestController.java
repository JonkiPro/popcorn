package com.jonki.popcorn.web.controller;

import com.jonki.popcorn.common.dto.User;
import com.jonki.popcorn.common.dto.error.ValidationErrorDTO;
import com.jonki.popcorn.common.dto.request.ForgotPasswordRequest;
import com.jonki.popcorn.common.dto.request.ForgotUsernameRequest;
import com.jonki.popcorn.common.exception.FormBadRequestException;
import com.jonki.popcorn.common.exception.ResourceNotFoundException;
import com.jonki.popcorn.core.properties.BundleProperties;
import com.jonki.popcorn.core.service.MailService;
import com.jonki.popcorn.core.service.UserPersistenceService;
import com.jonki.popcorn.core.service.UserSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ResourceBundle;

/**
 * REST end-point for supporting User data recovery.
 */
@RestController
@PreAuthorize("hasRole('ROLE_ANONYMOUS')")
@RequestMapping(value = "/api/v1.0/users/attributes")
@Slf4j
@Api(value = "Forgot API", description = "Provides a list of methods for reminding username and password")
public class ForgotRestController {

    private final ResourceBundle bundle = ResourceBundle.getBundle(BundleProperties.VALIDATION_MESSAGES.getSource(), LocaleContextHolder.getLocale());
    private final UserPersistenceService userPersistenceService;
    private final UserSearchService userSearchService;
    private final MailService mailService;

    /**
     * Constructor - initialize all required dependencies.
     *
     * @param userPersistenceService The user persistence service to use
     * @param userSearchService The user search service to use
     * @param mailService The mail service to use
     */
    @Autowired
    public ForgotRestController(
            final UserPersistenceService userPersistenceService,
            final UserSearchService userSearchService,
            final MailService mailService
    ) {
        this.userPersistenceService = userPersistenceService;
        this.userSearchService = userSearchService;
        this.mailService = mailService;
    }

    /**
     * Recover username and send it to the e-mail address.
     *
     * @param forgotUsernameRequest DTO with data to recover the username
     */
    @ApiOperation(value = "Recover username and send it to the e-mail address")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Incorrect data in the form or the user with such an e-mail doesn't exist"),
            @ApiResponse(code = 404, message = "No user found")
    })
    @PutMapping(value = "/username_recovery", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void forgotUsername(
            @ApiParam(value = "Username recovery form", required = true)
            @RequestBody @Valid final ForgotUsernameRequest forgotUsernameRequest
    ) {
        log.info("Called with forgotUsernameRequest {}", forgotUsernameRequest);

        this.validEmail(forgotUsernameRequest.getEmail());

        final User user = this.userSearchService.getUserByEmail(forgotUsernameRequest.getEmail());

        this.mailService.sendMailWithUsername(user.getEmail(), user.getUsername());
    }

    /**
     * Generate a new password for the user and send it to the e-mail address.
     *
     * @param forgotPasswordRequest DTO with data to generate a new password
     */
    @ApiOperation(value = "Generate a new password for the user and send it to the e-mail address")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Incorrect data in the form"),
            @ApiResponse(code = 404, message = "No user found")
    })
    @PutMapping(value = "/password_reset", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void forgotPassword(
            @ApiParam(value = "Password recovery form", required = true)
            @RequestBody @Valid final ForgotPasswordRequest forgotPasswordRequest
    ) {
        log.info("Called with forgotPasswordRequest {}", forgotPasswordRequest);

        this.validForgotPasswordRequest(forgotPasswordRequest);

        this.userPersistenceService.resetPassword(forgotPasswordRequest);
    }


    /**
     * Check if the e-mail address exists in the database.
     *
     * @param email The user's e-mail
     * @throws FormBadRequestException if the e-mail doesn't exist
     */
    private void validEmail(final String email) {
        if(!this.userSearchService.existsUserByEmail(email)) {
            final ValidationErrorDTO validationErrorDTO = new ValidationErrorDTO();
            validationErrorDTO.addFieldError("email", this.bundle.getString("validation.noExistsEmail"));

            throw new FormBadRequestException(validationErrorDTO);
        }
    }

    /**
     * Check if the username and e-mail address exists in the database.
     *
     * @param forgotPasswordRequest ForgotPasswordRequest object
     * @throws ResourceNotFoundException if no user found
     */
    private void validForgotPasswordRequest(final ForgotPasswordRequest forgotPasswordRequest) {
        if(!this.userSearchService.existsUserByUsername(forgotPasswordRequest.getUsername())
                && !this.userSearchService.existsUserByEmail(forgotPasswordRequest.getEmail())) {
            throw new ResourceNotFoundException("No user with this name and email was found");
        }
    }
}
