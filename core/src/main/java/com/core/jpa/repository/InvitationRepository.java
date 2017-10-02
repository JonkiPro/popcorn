package com.core.jpa.repository;

import com.core.jpa.entity.InvitationEntity;
import com.core.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Invitation repository.
 */
public interface InvitationRepository extends JpaRepository<InvitationEntity, Long> {

    /**
     * Find an invitation.
     *
     * @param fromUser From the user
     * @param toUser To the user
     * @return The invitation
     */
    Optional<InvitationEntity> findOneByFromUserAndToUser(UserEntity fromUser, UserEntity toUser);

    /**
     * Check if the invitation exists.
     *
     * @param fromUser From the user
     * @param toUser To the user
     * @return Whether the invitation exists
     */
    boolean existsByFromUserAndToUser(UserEntity fromUser, UserEntity toUser);
}
