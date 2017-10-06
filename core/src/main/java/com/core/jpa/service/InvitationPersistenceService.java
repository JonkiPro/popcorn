package com.core.jpa.service;

import com.common.exception.ResourceConflictException;
import com.common.exception.ResourceNotFoundException;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;

/**
 * Interfaces for providing persistence functions for invitations other than search.
 */
@Validated
public interface InvitationPersistenceService {

    /**
     * Create the invitation.
     *
     * @param fromId From the user ID
     * @param toId To the user ID
     * @return The id of the invitation created
     * @throws ResourceNotFoundException if no user found
     * @throws ResourceConflictException if invitation exists
     */
    Long createInvitation(
            @Min(1) final Long fromId,
            @Min(1) final Long toId
    ) throws ResourceNotFoundException, ResourceConflictException;


    /**
     * Delete the invitation.
     *
     * @param fromId From the user ID
     * @param toId To the user ID
     * @throws ResourceNotFoundException if no user found or no invitation found
     */
    void deleteInvitation(
            @Min(1) final Long fromId,
            @Min(1) final Long toId
    ) throws ResourceNotFoundException;
}
