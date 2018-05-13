package com.jonki.popcorn.web.controller;

import com.jonki.popcorn.common.dto.error.ValidationErrorDTO;
import com.jonki.popcorn.common.dto.request.ChangeEmailDTO;
import com.jonki.popcorn.common.dto.request.ChangePasswordDTO;
import com.jonki.popcorn.common.exception.FormBadRequestException;
import com.jonki.popcorn.core.properties.BundleProperties;
import com.jonki.popcorn.core.service.AuthorizationService;
import com.jonki.popcorn.core.service.UserPersistenceService;
import com.jonki.popcorn.core.service.UserSearchService;
import com.jonki.popcorn.core.util.EncryptUtils;
import com.jonki.popcorn.web.util.MultipartFileUtils;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    void updateEmail(
            @ApiParam(value = "Form of e-mail change", required = true)
            @RequestBody @Valid final ChangeEmailDTO changeEmailDTO
    ) {
        log.info("Called with {}", changeEmailDTO);

        this.validChangeEmailDTO(changeEmailDTO);

        this.userPersistenceService.updateNewEmail(changeEmailDTO);
    }

    @ApiOperation(value = "It changes the user's password")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Incorrect data in the form or password is incorrect"),
            @ApiResponse(code = 404, message = "No user found")
    })
    @PutMapping(value = "/password", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void updatePassword(
            @ApiParam(value = "Form of password change", required = true)
            @RequestBody @Valid final ChangePasswordDTO changePasswordDTO
    ) {
        log.info("Called with {}", changePasswordDTO);

        this.validChangePasswordDTO(changePasswordDTO);

        this.userPersistenceService.updatePassword(changePasswordDTO);
    }

    @ApiOperation(value = "It changes the user's avatar")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "No user found"),
            @ApiResponse(code = 412, message = "An I/O error occurs or incorrect content type"),
            @ApiResponse(code = 500, message = "An error occurred with the server")
    })
    @PutMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void updateAvatar(
            @ApiParam(value = "A new avatar for the user", required = true)
            @RequestPart final MultipartFile avatar
    ) {
        log.info("Called with avatar {}", avatar);

        this.userPersistenceService.updateAvatar(MultipartFileUtils.convert(avatar));
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
