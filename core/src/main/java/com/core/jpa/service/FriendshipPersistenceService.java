package com.core.jpa.service;

import com.common.exception.ResourceConflictException;
import com.common.exception.ResourceNotFoundException;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;

/**
 * Interfaces for providing persistence functions for friendship other than search.
 */
@Validated
public interface FriendshipPersistenceService {

    /**
     * Create the friendship.
     *
     * @param fromId From the user ID
     * @param toId To the user ID
     * @throws ResourceNotFoundException if no user found
     * @throws ResourceConflictException if friendship exists
     */
    void createFriendship(
            @Min(1) final Long fromId,
            @Min(1) final Long toId
    ) throws ResourceNotFoundException, ResourceConflictException;


    /**
     * Delete the friendship.
     *
     * @param fromId From the user ID
     * @param toId To the user ID
     * @throws ResourceNotFoundException if no user found or no friendship found
     */
    void deleteFriendship(
            @Min(1) final Long fromId,
            @Min(1) final Long toId
    ) throws ResourceNotFoundException;
}
