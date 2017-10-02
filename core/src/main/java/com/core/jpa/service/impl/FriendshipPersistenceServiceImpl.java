package com.core.jpa.service.impl;

import com.common.exception.ResourceConflictException;
import com.common.exception.ResourceNotFoundException;
import com.core.jpa.entity.FriendshipEntity;
import com.core.jpa.entity.UserEntity;
import com.core.jpa.repository.FriendshipRepository;
import com.core.jpa.repository.UserRepository;
import com.core.jpa.service.FriendshipPersistenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * JPA implementation of the FriendshipEntity Persistence Service.
 */
@Service("friendshipPersistenceService")
@Slf4j
@Validated
public class FriendshipPersistenceServiceImpl implements FriendshipPersistenceService {

    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;

    /**
     * Constructor.
     *
     * @param friendshipRepository The friendship repository to use
     * @param userRepository The user repository to use
     */
    @Autowired
    public FriendshipPersistenceServiceImpl(
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
    public void createFriendship(
            @Min(1) final Long fromId,
            @Min(1) final Long toId
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with id {}, id {}", fromId, toId);

        Optional<UserEntity> fromUser = this.userRepository.findByIdAndEnabledTrue(fromId);
        fromUser.orElseThrow(() -> new ResourceNotFoundException("No user found with id " + fromId));

        Optional<UserEntity> toUser = this.userRepository.findByIdAndEnabledTrue(toId);
        toUser.orElseThrow(() -> new ResourceNotFoundException("No user found with id " + toId));

        if(this.friendshipRepository.existsByFromUserAndToUser(fromUser.get(), toUser.get())
                && this.friendshipRepository.existsByFromUserAndToUser(toUser.get(), fromUser.get())) {
            throw new ResourceConflictException("The friendship exists between id " + fromId + " id " + toId);
        }

        this.friendshipRepository.save(new FriendshipEntity(fromUser.get(), toUser.get()));
        this.friendshipRepository.save(new FriendshipEntity(toUser.get(), fromUser.get()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteFriendship(
            @Min(1) final Long fromId,
            @Min(1) final Long toId
    ) throws ResourceNotFoundException {
        log.info("Called with id {}, id {}", fromId, toId);

        Optional<UserEntity> fromUser = this.userRepository.findByIdAndEnabledTrue(fromId);
        fromUser.orElseThrow(() -> new ResourceNotFoundException("No user found with id " + fromId));

        Optional<UserEntity> toUser = this.userRepository.findByIdAndEnabledTrue(toId);
        toUser.orElseThrow(() -> new ResourceNotFoundException("No user found with id " + toId));

        if(!this.friendshipRepository.existsByFromUserAndToUser(fromUser.get(), toUser.get())
                && !this.friendshipRepository.existsByFromUserAndToUser(toUser.get(), fromUser.get())) {
            throw new ResourceNotFoundException("No friendship found between id " + fromId + " id " + toId);
        }

        this.friendshipRepository.delete(this.friendshipRepository.findOneByFromUserAndToUser(fromUser.get(), toUser.get()).get());
        this.friendshipRepository.delete(this.friendshipRepository.findOneByFromUserAndToUser(toUser.get(), fromUser.get()).get());
    }
}
