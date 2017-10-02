package com.core.jpa.service.impl;

import com.common.exception.ResourceConflictException;
import com.common.exception.ResourceNotFoundException;
import com.core.jpa.entity.InvitationEntity;
import com.core.jpa.entity.UserEntity;
import com.core.jpa.repository.InvitationRepository;
import com.core.jpa.repository.UserRepository;
import com.core.jpa.service.InvitationPersistenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * JPA implementation of the InvitationEntity Persistence Service.
 */
@Service("invitationPersistenceService")
@Slf4j
@Validated
public class InvitationPersistenceServiceImpl implements InvitationPersistenceService {

    private final InvitationRepository invitationRepository;
    private final UserRepository userRepository;

    /**
     * Constructor.
     *
     * @param invitationRepository The invitation repository to use
     * @param userRepository The user repository to use
     */
    @Autowired
    public InvitationPersistenceServiceImpl(
            @NotNull final InvitationRepository invitationRepository,
            @NotNull final UserRepository userRepository
    ) {
        this.invitationRepository = invitationRepository;
        this.userRepository = userRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createInvitation(
            @Min(1) final Long fromId,
            @Min(1) final Long toId
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with id {}, id {}", fromId, toId);

        Optional<UserEntity> fromUser = this.userRepository.findByIdAndEnabledTrue(fromId);
        fromUser.orElseThrow(() -> new ResourceNotFoundException("No user found with id " + fromId));

        Optional<UserEntity> toUser = this.userRepository.findByIdAndEnabledTrue(toId);
        toUser.orElseThrow(() -> new ResourceNotFoundException("No user found with id " + toId));

        if(this.invitationRepository.existsByFromUserAndToUser(fromUser.get(), toUser.get())
                || this.invitationRepository.existsByFromUserAndToUser(toUser.get(), fromUser.get())) {
            throw new ResourceConflictException("The invitation exists between id " + fromId + " id " + toId);
        }

        this.invitationRepository.save(new InvitationEntity(fromUser.get(), toUser.get()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteInvitation(
            @Min(1) final Long fromId,
            @Min(1) final Long toId
    ) throws ResourceNotFoundException {
        log.info("Called with id {}, id {}", fromId, toId);

        Optional<UserEntity> fromUser = this.userRepository.findByIdAndEnabledTrue(fromId);
        fromUser.orElseThrow(() -> new ResourceNotFoundException("No user found with id " + fromId));

        Optional<UserEntity> toUser = this.userRepository.findByIdAndEnabledTrue(toId);
        toUser.orElseThrow(() -> new ResourceNotFoundException("No user found with id " + toId));

        Optional<InvitationEntity> invitation = this.invitationRepository.findOneByFromUserAndToUser(fromUser.get(), toUser.get());

        invitation.orElseThrow(() -> new ResourceNotFoundException("No invitation found between id " + fromId + " id " + toId));

        this.invitationRepository.delete(invitation.get());
    }
}
