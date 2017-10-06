package com.core.jpa.service.impl;

import com.common.exception.ResourceNotFoundException;
import com.core.jpa.entity.UserEntity;
import com.core.jpa.repository.FriendshipRepository;
import com.core.jpa.repository.UserRepository;
import com.core.jpa.service.FriendshipSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * JPA implementation of the FriendshipEntity Search Service.
 */
@Service("friendshipSearchService")
@Slf4j
@Transactional(readOnly = true)
@Validated
public class FriendshipSearchServiceImpl implements FriendshipSearchService {

    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;

    /**
     * Constructor.
     *
     * @param friendshipRepository The friendship repository to use
     * @param userRepository The user repository to use
     */
    @Autowired
    public FriendshipSearchServiceImpl(
            @NotNull final FriendshipRepository friendshipRepository,
            @NotNull final UserRepository userRepository
    ) {
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getFriendshipExists(
            @Min(1) final Long fromId,
            @Min(1) final Long toId
    ) throws ResourceNotFoundException {
        log.info("Called with id {}, id {}", fromId, toId);

        final Optional<UserEntity> fromUser = this.userRepository.findByIdAndEnabledTrue(fromId);
        fromUser.orElseThrow(() -> new ResourceNotFoundException("No user found with id " + fromId));

        final Optional<UserEntity> toUser = this.userRepository.findByIdAndEnabledTrue(toId);
        toUser.orElseThrow(() -> new ResourceNotFoundException("No user found with id " + toId));

        return this.friendshipRepository.existsByFromUserAndToUser(fromUser.get(), toUser.get());
    }
}
