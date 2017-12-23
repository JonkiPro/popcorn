package com.web.web.security.service;

import com.common.dto.User;

/**
 * The service operates on the data of an authorised user.
 */
public interface AuthorizationService {

    /**
     * Get an authorized user object.
     *
     * @return Returns the user object
     */
    User getUser();

    /**
     * Get an authorised user ID.
     *
     * @return Returns the user ID
     */
    String getUserId();

    /**
     * Get an authorised username.
     *
     * @return Returns the username
     */
    String getUsername();
}
