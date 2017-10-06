package com.web.web.controller;

import com.common.dto.error.ValidationErrorDTO;
import com.common.dto.request.ForgotPasswordDTO;
import com.common.dto.User;
import com.common.exception.FormBadRequestException;
import com.common.dto.request.ForgotUsernameDTO;
import com.common.exception.ResourceNotFoundException;
import com.core.jpa.service.UserPersistenceService;
import com.core.jpa.service.UserSearchService;
import com.core.properties.BundleProperties;
import com.core.service.MailService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ResourceBundle;

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

    @ApiOperation(value = "Recover username and send it to the e-mail address")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Incorrect data in the form or the user with such an e-mail doesn't exist"),
            @ApiResponse(code = 404, message = "No user found")
    })
    @PutMapping(value = "/username_recovery", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void forgotUsername(
            @ApiParam(value = "Username recovery form", required = true) @RequestBody @Valid final ForgotUsernameDTO forgotUsernameDTO
    ) {
        log.info("Called with {}", forgotUsernameDTO);

        this.validEmail(forgotUsernameDTO.getEmail());

        final User user = this.userSearchService.getUserByEmail(forgotUsernameDTO.getEmail());

        this.mailService.sendMailWithUsername(user.getEmail(), user.getUsername());
    }

    @ApiOperation(value = "Generate a new password for the user and send it to the e-mail address")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Incorrect data in the form"),
            @ApiResponse(code = 404, message = "No user found")
    })
    @PutMapping(value = "/password_reset", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void forgotPassword(
            @ApiParam(value = "Password recovery form", required = true) @RequestBody @Valid final ForgotPasswordDTO forgotPasswordDTO
    ) {
        log.info("Called with {}", forgotPasswordDTO);

        this.validForgotPasswordDTO(forgotPasswordDTO);

        this.userPersistenceService.resetPassword(forgotPasswordDTO);
    }


    /**
     * Check if the e-mail address exists in the database.
     *
     * @param email The user's e-mail
     * @throws FormBadRequestException if the e-mail doesn't exist
     */
    private void validEmail(final String email) {
        if(!this.userSearchService.getUserExistsByEmail(email)) {
            final ValidationErrorDTO validationErrorDTO = new ValidationErrorDTO();
            validationErrorDTO.addFieldError("email", this.bundle.getString("validation.noExistsEmail"));

            throw new FormBadRequestException(validationErrorDTO);
        }
    }

    /**
     * Check if the username or e-mail address exists in the database.
     *
     * @param forgotPasswordDTO ForgotPasswordDTO object
     * @throws ResourceNotFoundException if no user found
     */
    private void validForgotPasswordDTO(final ForgotPasswordDTO forgotPasswordDTO) {
        if(!this.userSearchService.getUserExistsByUsername(forgotPasswordDTO.getUsername())
                || !this.userSearchService.getUserExistsByEmail(forgotPasswordDTO.getEmail())) {
            throw new ResourceNotFoundException("No user with this name and email was found");
        }
    }
}
