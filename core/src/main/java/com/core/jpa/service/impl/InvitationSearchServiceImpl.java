package com.core.jpa.service.impl;

import com.common.exception.ResourceNotFoundException;
import com.core.jpa.entity.UserEntity;
import com.core.jpa.repository.InvitationRepository;
import com.core.jpa.repository.UserRepository;
import com.core.jpa.service.InvitationSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * JPA implementation of the InvitationEntity Search Service.
 */
@Service("invitationSearchService")
@Slf4j
@Transactional(readOnly = true)
@Validated
public class InvitationSearchServiceImpl implements InvitationSearchService {

    private final InvitationRepository invitationRepository;
    private final UserRepository userRepository;

    /**
     * Constructor.
     *
     * @param invitationRepository The invitation repository to use
     * @param userRepository The user repository to use
     */
    @Autowired
    public InvitationSearchServiceImpl(
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
    public boolean getInvitationExists(
            @Min(1) final Long fromId,
            @Min(1) final Long toId
    ) throws ResourceNotFoundException {
        log.info("Called with id {}, id {}", fromId, toId);

        final Optional<UserEntity> fromUser = this.userRepository.findByIdAndEnabledTrue(fromId);
        fromUser.orElseThrow(() -> new ResourceNotFoundException("No user found with id " + fromId));

        final Optional<UserEntity> toUser = this.userRepository.findByIdAndEnabledTrue(toId);
        toUser.orElseThrow(() -> new ResourceNotFoundException("No user found with id " + toId));

        return this.invitationRepository.existsByFromUserAndToUser(fromUser.get(), toUser.get());
    }
}
