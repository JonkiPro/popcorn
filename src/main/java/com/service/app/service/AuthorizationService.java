package com.service.app.service;

import com.service.app.entity.User;

/**
 * The service works on authorized data.
 */
public interface AuthorizationService {

    /**
     * This method returns the user.
     * @return Returns the user.
     */
    User getUser();

    /**
     * This method returns the username.
     * @return Returns the username.
     */
    String getUsername();
}
