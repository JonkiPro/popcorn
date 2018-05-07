package com.jonki.popcorn.common.dto;

/**
 * Status of relations between users.
 */
public enum RelationshipStatus {

    /**
     * Users are friends
     */
    FRIEND,

    /**
     * Invitation from the user
     */
    INVITATION_FROM_YOU,

    /**
     * Invitation to the user
     */
    INVITATION_TO_YOU,

    /**
     * No relationship
     */
    UNKNOWN
}
