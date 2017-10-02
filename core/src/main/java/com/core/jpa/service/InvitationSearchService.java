package com.core.jpa.service;

import com.common.exception.ResourceNotFoundException;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;

/**
 * Interface for searching invitations.
 */
@Validated
public interface InvitationSearchService {

    /**
     * Check whether the invitation exists.
     *
     * @param fromId From the user ID
     * @param toId To the user ID
     * @return Value of true or false
     * @throws ResourceNotFoundException if no user found
     */
    boolean getInvitationExists(
            @Min(1) final Long fromId,
            @Min(1) final Long toId
    ) throws ResourceNotFoundException;
}
