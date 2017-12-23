package com.core.jpa.service;

import com.common.dto.request.ChangeEmailDTO;
import com.common.dto.request.ChangePasswordDTO;
import com.common.dto.request.ForgotPasswordDTO;
import com.common.dto.request.RegisterDTO;
import com.common.exception.ResourceBadRequestException;
import com.common.exception.ResourceConflictException;
import com.common.exception.ResourceNotFoundException;
import com.core.jpa.entity.UserEntity;
import com.core.jpa.repository.UserRepository;
import com.core.service.UserPersistenceService;
import com.common.dto.SecurityRole;
import com.common.dto.UserMoviePermission;
import com.core.service.MailService;
import com.core.utils.EncryptUtils;
import com.core.utils.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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
                ConstraintViolationException.class
        }
)
@Validated
public class UserPersistenceServiceImpl implements UserPersistenceService {

    private final UserRepository userRepository;
    private final MailService mailService;

    /**
     * Constructor.
     *
     * @param userRepository The user repository to use
     * @param mailService The mail service to use
     */
    @Autowired
    public UserPersistenceServiceImpl(
            @NotNull final UserRepository userRepository,
            @NotNull final MailService mailService
    ) {
        this.userRepository = userRepository;
        this.mailService = mailService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createUser(
            @NotNull @Valid final RegisterDTO registerDTO
    ) throws ResourceConflictException {
        log.info("Called with registerDTO {}", registerDTO);

        if(userRepository.existsByUsernameIgnoreCase(registerDTO.getUsername())) {
            throw new ResourceConflictException("The username " + registerDTO.getUsername() + " exists");
        } else if(userRepository.existsByEmailIgnoreCase(registerDTO.getEmail())) {
            throw new ResourceConflictException("The e-mail " + registerDTO.getEmail() + " exists");
        }

        final UserEntity user = this.registerDtoToUserEntity(registerDTO);
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
            @NotNull @Valid final ForgotPasswordDTO forgotPasswordDTO
    ) throws ResourceNotFoundException {
        log.info("Called with forgotPasswordDTO {}", forgotPasswordDTO);

        final UserEntity user
                = this.userRepository
                     .findByUsernameIgnoreCaseAndEmailIgnoreCaseAndEnabledTrue(forgotPasswordDTO.getUsername(), forgotPasswordDTO.getEmail())
                         .orElseThrow(
                                 () -> new ResourceNotFoundException("No user found with username "
                                                                     + forgotPasswordDTO.getUsername()
                                                                     + ", e-mail "
                                                                     + forgotPasswordDTO.getEmail())
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
            @NotBlank final String id,
            @NotNull @Valid final ChangeEmailDTO changeEmailDTO
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with id {}, changeEmailDTO {}", id, changeEmailDTO);

        final UserEntity user = this.findUser(id);

        if(this.userRepository.existsByEmailIgnoreCase(changeEmailDTO.getEmail())) {
            throw new ResourceConflictException("The e-mail " + changeEmailDTO.getEmail() + " exists");
        }

        user.setEmailChangeToken(RandomUtils.randomToken());
        user.setNewEmail(changeEmailDTO.getEmail());

        this.mailService.sendMailWithEmailChangeToken(user.getEmail(), user.getEmailChangeToken());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePassword(
            @NotBlank final String id,
            @NotNull @Valid final ChangePasswordDTO changePasswordDTO
    ) throws ResourceNotFoundException, ResourceBadRequestException {
        log.info("Called with id {}, changePasswordDTO {}", id, changePasswordDTO);

        final UserEntity user = this.findUser(id);

        if(!EncryptUtils.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
            throw new ResourceBadRequestException("The entered password doesn't match the old password");
        }

        user.setPassword(EncryptUtils.encrypt(changePasswordDTO.getNewPassword()));
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
            @NotBlank final String fromId,
            @NotBlank final String toId
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with fromId {}, toId {}", fromId, toId);

        final UserEntity authUser = this.findUser(fromId);
        final UserEntity sendingInvitationUser = this.findUser(toId);

        sendingInvitationUser.removeSentInvitation(authUser);
        authUser.addFriend(sendingInvitationUser);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeFriend(
            @NotBlank final String fromId,
            @NotBlank final String toId
    ) throws ResourceNotFoundException {
        log.info("Called with fromId {}, toId {}", fromId, toId);

        this.findUser(fromId).removeFriend(this.findUser(toId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addInvitation(
            @NotBlank final String fromId,
            @NotBlank final String toId
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with fromId {}, toId {}", fromId, toId);

        final UserEntity fromUser = this.findUser(fromId);
        final UserEntity toUser = this.findUser(toId);

        if(toUser.getSentInvitations().contains(fromUser)) {
            throw new ResourceConflictException("There is an invitation from the user with ID " + toUser.getUniqueId());
        }

        fromUser.addSentInvitation(toUser);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeInvitation(
            @NotBlank final String fromId,
            @NotBlank final String toId
    ) throws ResourceNotFoundException {
        log.info("Called with fromId {}, toId {}", fromId, toId);

        this.findUser(fromId).removeSentInvitation(this.findUser(toId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createAdmin(
            RegisterDTO registerDTO
    ) throws ResourceConflictException {
        log.info("Called with ", registerDTO);

        if(userRepository.existsByUsernameIgnoreCase(registerDTO.getUsername())) {
            throw new ResourceConflictException("The username " + registerDTO.getUsername() + " exists");
        } else if(userRepository.existsByEmailIgnoreCase(registerDTO.getEmail())) {
            throw new ResourceConflictException("The e-mail " + registerDTO.getEmail() + " exists");
        }

        final UserEntity user = this.registerDtoToUserEntity(registerDTO);
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
     * Converter RegisterDTO to UserEntity.
     *
     * @param registerDTO RegisterDTO object
     * @return The user entity
     */
    private UserEntity registerDtoToUserEntity(final RegisterDTO registerDTO) {
        final UserEntity user = new UserEntity();
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(EncryptUtils.encrypt(registerDTO.getPassword()));

        return user;
    }
}
