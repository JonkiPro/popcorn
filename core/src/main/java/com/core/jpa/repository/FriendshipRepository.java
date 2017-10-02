package com.core.jpa.repository;

import com.core.jpa.entity.FriendshipEntity;
import com.core.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Friendship repository.
 */
public interface FriendshipRepository extends JpaRepository<FriendshipEntity, Long> {

    /**
     * Find a friendship.
     *
     * @param fromUser From the user
     * @param toUser To the user
     * @return The friendship
     */
    Optional<FriendshipEntity> findOneByFromUserAndToUser(UserEntity fromUser, UserEntity toUser);

    /**
     * Check if the friendship exists.
     *
     * @param fromUser From the user
     * @param toUser To the user
     * @return Whether the friendship exists
     */
    boolean existsByFromUserAndToUser(UserEntity fromUser, UserEntity toUser);
}
