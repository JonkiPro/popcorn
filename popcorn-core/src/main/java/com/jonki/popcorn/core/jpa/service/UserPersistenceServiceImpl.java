package com.jonki.popcorn.core.jpa.service;

import com.jonki.popcorn.common.dto.SecurityRole;
import com.jonki.popcorn.common.dto.StorageDirectory;
import com.jonki.popcorn.common.dto.StorageProvider;
import com.jonki.popcorn.common.dto.UserMoviePermission;
import com.jonki.popcorn.common.dto.request.ChangeEmailRequest;
import com.jonki.popcorn.common.dto.request.ChangePasswordRequest;
import com.jonki.popcorn.common.dto.request.ForgotPasswordRequest;
import com.jonki.popcorn.common.dto.request.RegisterRequest;
import com.jonki.popcorn.common.exception.ResourceBadRequestException;
import com.jonki.popcorn.common.exception.ResourceConflictException;
import com.jonki.popcorn.common.exception.ResourceNotFoundException;
import com.jonki.popcorn.common.exception.ResourcePreconditionException;
import com.jonki.popcorn.common.exception.ResourceServerException;
import com.jonki.popcorn.core.jpa.entity.UserEntity;
import com.jonki.popcorn.core.jpa.repository.UserRepository;
import com.jonki.popcorn.core.service.AuthorizationService;
import com.jonki.popcorn.core.service.MailService;
import com.jonki.popcorn.core.service.StorageService;
import com.jonki.popcorn.core.service.UserPersistenceService;
import com.jonki.popcorn.core.util.EncryptUtils;
import com.jonki.popcorn.core.util.FileUtils;
import com.jonki.popcorn.core.util.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.EnumSet;

/**
 * JPA implementation of the User Persistence Service.
 */
@Service("userPersistenceService")
@Slf4j
@Transactional(
        rollbackFor = {
                ResourceBadRequestException.class,
                ResourceNotFoundException.class,
                ResourceConflictException.class,
                ResourcePreconditionException.class,
                ResourceServerException.class,
                ConstraintViolationException.class
        }
)
@Validated
public class UserPersistenceServiceImpl implements UserPersistenceService {

    private final UserRepository userRepository;
    private final MailService mailService;
    private final StorageService storageService;
    private final AuthorizationService authorizationService;

    /**
     * Constructor.
     *
     * @param userRepository The user repository to use
     * @param mailService The mail service to use
     * @param storageService The storage service to use
     * @param authorizationService The authorization service to use
     */
    @Autowired
    public UserPersistenceServiceImpl(
            @NotNull final UserRepository userRepository,
            @NotNull final MailService mailService,
            @Qualifier("googleStorageService") @NotNull final StorageService storageService,
            @NotNull final AuthorizationService authorizationService
    ) {
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.storageService = storageService;
        this.authorizationService = authorizationService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createUser(
            @NotNull @Valid final RegisterRequest registerRequest
    ) throws ResourceConflictException {
        log.info("Called with registerRequest {}", registerRequest);

        if(userRepository.existsByUsernameIgnoreCase(registerRequest.getUsername())) {
            throw new ResourceConflictException("The username " + registerRequest.getUsername() + " exists");
        } else if(userRepository.existsByEmailIgnoreCase(registerRequest.getEmail())) {
            throw new ResourceConflictException("The e-mail " + registerRequest.getEmail() + " exists");
        }

        final UserEntity user = this.registerRequestToUserEntity(registerRequest);
        user.setActivationToken(RandomUtils.randomToken());
        user.setAuthorities(EnumSet.of(SecurityRole.ROLE_USER));

        this.mailService.sendMailWithActivationToken(user.getEmail(), user.getActivationToken());

        return this.userRepository.save(user).getUniqueId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void activationUser(
            @NotBlank final String token
    ) throws ResourceNotFoundException {
        log.info("Called with token {}", token);

        final UserEntity user
                = this.userRepository.findByActivationToken(token)
                         .orElseThrow(() -> new ResourceNotFoundException("No user found with token " + token));

        user.setActivationToken(null);
        user.setEnabled(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetPassword(
            @NotNull @Valid final ForgotPasswordRequest forgotPasswordRequest
    ) throws ResourceNotFoundException {
        log.info("Called with forgotPasswordRequest {}", forgotPasswordRequest);

        final UserEntity user
                = this.userRepository
                     .findByUsernameIgnoreCaseAndEmailIgnoreCaseAndEnabledTrue(forgotPasswordRequest.getUsername(), forgotPasswordRequest.getEmail())
                         .orElseThrow(
                                 () -> new ResourceNotFoundException("No user found with username "
                                                                     + forgotPasswordRequest.getUsername()
                                                                     + ", e-mail "
                                                                     + forgotPasswordRequest.getEmail())
                         );

        final String newPassword = RandomUtils.randomPassword();

        user.setPassword(EncryptUtils.encrypt(newPassword));

        this.mailService.sendMailWithNewPassword(user.getEmail(), newPassword);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateNewEmail(
            @NotNull @Valid final ChangeEmailRequest changeEmailRequest
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with changeEmailRequest {}", changeEmailRequest);

        final UserEntity user = this.findUser(this.authorizationService.getUserId());

        if(this.userRepository.existsByEmailIgnoreCase(changeEmailRequest.getEmail())) {
            throw new ResourceConflictException("The e-mail " + changeEmailRequest.getEmail() + " exists");
        }

        user.setEmailChangeToken(RandomUtils.randomToken());
        user.setNewEmail(changeEmailRequest.getEmail());

        this.mailService.sendMailWithEmailChangeToken(user.getEmail(), user.getEmailChangeToken());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePassword(
            @NotNull @Valid final ChangePasswordRequest changePasswordRequest
    ) throws ResourceNotFoundException, ResourceBadRequestException {
        log.info("Called with changePasswordRequest {}", changePasswordRequest);

        final UserEntity user = this.findUser(this.authorizationService.getUserId());

        if(!EncryptUtils.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new ResourceBadRequestException("The entered password doesn't match the old password");
        }

        user.setPassword(EncryptUtils.encrypt(changePasswordRequest.getNewPassword()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAvatar(
            @NotNull final File file
    ) throws ResourceBadRequestException, ResourceNotFoundException, ResourcePreconditionException, ResourceServerException {
        log.info("Called with file {}", file);
        FileUtils.validImage(file);

        final UserEntity user = this.findUser(this.authorizationService.getUserId());

        user.setAvatarId(this.storageService.save(file, StorageDirectory.AVATAR));
        user.setAvatarProvider(StorageProvider.GOOGLE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateEmail(
            @NotBlank final String token
    ) throws ResourceNotFoundException {
        log.info("Called with token {}", token);

        final UserEntity user
                = this.userRepository.findByEmailChangeToken(token)
                         .orElseThrow(() -> new ResourceNotFoundException("No user found with token " + token));

        user.setEmail(user.getNewEmail());
        user.setEmailChangeToken(null);
        user.setNewEmail(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addFriend(
            @NotBlank final String id
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with id {}", id);

        final UserEntity authUser = this.findUser(this.authorizationService.getUserId());
        final UserEntity sendingInvitationUser = this.findUser(id);

        sendingInvitationUser.removeSentInvitation(authUser);
        authUser.addFriend(sendingInvitationUser);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeFriend(
            @NotBlank final String id
    ) throws ResourceNotFoundException {
        log.info("Called with id {}", id);

        this.findUser(this.authorizationService.getUserId()).removeFriend(this.findUser(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addInvitation(
            @NotBlank final String id
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with id {}", id);

        final UserEntity authUser = this.findUser(this.authorizationService.getUserId());
        final UserEntity toUser = this.findUser(id);

        if(toUser.getSentInvitations().contains(authUser)) {
            throw new ResourceConflictException("There is an invitation from the user with ID " + toUser.getUniqueId());
        }

        authUser.addSentInvitation(toUser);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeInvitation(
            @NotBlank final String id
    ) throws ResourceNotFoundException {
        log.info("Called with id {}", id);

        this.findUser(this.authorizationService.getUserId()).removeSentInvitation(this.findUser(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rejectInvitation(
            @NotBlank final String id
    ) throws ResourceNotFoundException {
        log.info("Called with id {}", id);

        this.findUser(id).removeSentInvitation(this.findUser(this.authorizationService.getUserId()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createAdmin(
            final RegisterRequest registerRequest
    ) throws ResourceConflictException {
        log.info("Called with registerRequest {}", registerRequest);

        if(userRepository.existsByUsernameIgnoreCase(registerRequest.getUsername())) {
            throw new ResourceConflictException("The username " + registerRequest.getUsername() + " exists");
        } else if(userRepository.existsByEmailIgnoreCase(registerRequest.getEmail())) {
            throw new ResourceConflictException("The e-mail " + registerRequest.getEmail() + " exists");
        }

        final UserEntity user = this.registerRequestToUserEntity(registerRequest);
        user.setAuthorities(EnumSet.of(SecurityRole.ROLE_USER, SecurityRole.ROLE_ADMIN));
        user.setPermissions(EnumSet.of(UserMoviePermission.ALL));
        user.setEnabled(true);

        return this.userRepository.save(user).getUniqueId();
    }

    /**
     * Helper method to find the user entity.
     *
     * @param id The user ID
     * @return The user
     * @throws ResourceNotFoundException if no user found
     */
    private UserEntity findUser(final String id) throws ResourceNotFoundException {
        return this.userRepository
                .findByUniqueIdAndEnabledTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with id " + id));
    }

    /**
     * Converter RegisterRequest to UserEntity.
     *
     * @param registerRequest RegisterRequest object
     * @return The user entity
     */
    private UserEntity registerRequestToUserEntity(final RegisterRequest registerRequest) {
        final UserEntity user = new UserEntity();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(EncryptUtils.encrypt(registerRequest.getPassword()));

        return user;
    }
}
