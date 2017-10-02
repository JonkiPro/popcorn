package com.web.web.security.service;

import com.common.dto.User;

/**
 * The service operates on the data of an authorised user.
 */
public interface AuthorizationService {

    /**
     * This method returns the user object.
     *
     * @return Returns the user object
     */
    User getUser();

    /**
     * This method returns the user ID.
     *
     * @return Returns the user ID
     */
    Long getUserId();

    /**
     * This method returns the username.
     *
     * @return Returns the username
     */
    String getUsername();
}
