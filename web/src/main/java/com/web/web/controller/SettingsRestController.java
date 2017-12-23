package com.web.web.controller;

import com.common.dto.error.ValidationErrorDTO;
import com.common.dto.request.ChangeEmailDTO;
import com.common.dto.request.ChangePasswordDTO;
import com.common.exception.FormBadRequestException;
import com.core.service.UserPersistenceService;
import com.core.service.UserSearchService;
import com.core.properties.BundleProperties;
import com.core.utils.EncryptUtils;
import com.web.web.security.service.AuthorizationService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ResourceBundle;

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping(value = "/api/v1.0/users/account/update")
@Slf4j
@Api(value = "Settings API", description = "Provides a list of methods that manage user settings")
public class SettingsRestController {

    private final ResourceBundle bundle = ResourceBundle.getBundle(BundleProperties.VALIDATION_MESSAGES.getSource(), LocaleContextHolder.getLocale());
    private final UserPersistenceService userPersistenceService;
    private final UserSearchService userSearchService;
    private final AuthorizationService authorizationService;

    /**
     * Constructor - initialize all required dependencies.
     *
     * @param userPersistenceService The user persistence service to use
     * @param userSearchService The user search service to use
     * @param authorizationService The authorization service to use
     */
    @Autowired
    public SettingsRestController(
            final UserPersistenceService userPersistenceService,
            final UserSearchService userSearchService,
            final AuthorizationService authorizationService
    ) {
        this.userPersistenceService = userPersistenceService;
        this.userSearchService = userSearchService;
        this.authorizationService = authorizationService;
    }

    @ApiOperation(value = "It changes the user's email and sends a token to the mail")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Incorrect data in the form"),
            @ApiResponse(code = 404, message = "No user found"),
            @ApiResponse(code = 409, message = "The e-mail exists")
    })
    @PutMapping(value = "/email", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void changeEmail(
            @ApiParam(value = "Form of e-mail change", required = true)
            @RequestBody @Valid final ChangeEmailDTO changeEmailDTO
    ) {
        log.info("Called with {}", changeEmailDTO);

        this.validChangeEmailDTO(changeEmailDTO);

        this.userPersistenceService.updateNewEmail(this.authorizationService.getUserId(), changeEmailDTO);
    }

    @ApiOperation(value = "It changes the user's password")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Incorrect data in the form or password is incorrect"),
            @ApiResponse(code = 404, message = "No user found")
    })
    @PutMapping(value = "/password", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void changePassword(
            @ApiParam(value = "Form of password change", required = true)
            @RequestBody @Valid final ChangePasswordDTO changePasswordDTO
    ) {
        log.info("Called with {}", changePasswordDTO);

        this.validChangePasswordDTO(changePasswordDTO);

        this.userPersistenceService.updatePassword(this.authorizationService.getUserId(), changePasswordDTO);
    }

    @ApiOperation(value = "Activates e-mail change with token")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "Token not found") })
    @PreAuthorize("permitAll()")
    @PutMapping(value = "/email/token/{token}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void emailChangeToken(
            @ApiParam(value = "E-mail change token", required = true)
            @PathVariable("token") final String token
    ) {
        log.info("Called with token {}", token);

        this.userPersistenceService.updateEmail(token);
    }


    /**
     * Check that the password is correct and that there is a user with the given email address.
     *
     * @param changeEmailDTO ChangeEmailDTO object
     * @throws FormBadRequestException if any data problem
     */
    private void validChangeEmailDTO(final ChangeEmailDTO changeEmailDTO) {
        final ValidationErrorDTO validationErrorDTO = new ValidationErrorDTO();

        if(this.userSearchService.existsUserByEmail(changeEmailDTO.getEmail())) {
            validationErrorDTO.addFieldError("email", bundle.getString("validation.existsEmail"));
        }
        final String userPassword = this.userSearchService.getUserPassword(this.authorizationService.getUsername());
        if(!EncryptUtils.matches(changeEmailDTO.getPassword(), userPassword)) {
            validationErrorDTO.addFieldError("password", bundle.getString("validation.passwordsAreDifferent"));
        }

        if(!validationErrorDTO.getFieldErrors().isEmpty()) {
            throw new FormBadRequestException(validationErrorDTO);
        }
    }

    /**
     * Check that the passwords are correct.
     *
     * @param changePasswordDTO ChangePasswordDTO object
     * @throws FormBadRequestException if any data problem
     */
    private void validChangePasswordDTO(final ChangePasswordDTO changePasswordDTO) {
        final ValidationErrorDTO validationErrorDTO = new ValidationErrorDTO();

        final String userPassword = this.userSearchService.getUserPassword(this.authorizationService.getUsername());

        if(!EncryptUtils.matches(changePasswordDTO.getOldPassword(), userPassword)) {
            validationErrorDTO.addFieldError("oldPassword", bundle.getString("validation.passwordsAreDifferent"));
        }
        if(EncryptUtils.matches(changePasswordDTO.getNewPassword(), userPassword)) {
            validationErrorDTO.addFieldError("newPassword", bundle.getString("validation.passwordsAreTheSame"));
        }

        if(!validationErrorDTO.getFieldErrors().isEmpty()) {
            throw new FormBadRequestException(validationErrorDTO);
        }
    }
}
