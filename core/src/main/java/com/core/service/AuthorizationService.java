package com.core.service;

import org.springframework.security.access.prepost.PreAuthorize;

/**
 * The service operates on the data of an authorised user.
 */
public interface AuthorizationService {

    /**
     * Get an authorised user ID.
     *
     * @return Returns the user ID
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    String getUserId();

    /**
     * Get an authorised username.
     *
     * @return Returns the username
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    String getUsername();

    /**
     * Check if the user is logged in.
     *
     * @return True if the user is logged in
     */
    boolean isLogged();
}
