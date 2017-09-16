package com.service.app.service;

/**
 * The service works on authorized data.
 */
public interface AuthorizationService {

    /**
     * This method returns the user ID.
     * @return Returns the user ID.
     */
    Long getUserId();

    /**
     * This method returns the user username.
     * @return Returns the user username.
     */
    String getUserUsername();
}
