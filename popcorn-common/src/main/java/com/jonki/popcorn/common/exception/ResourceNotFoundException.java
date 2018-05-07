package com.jonki.popcorn.common.exception;

import java.net.HttpURLConnection;

/**
 * Exception for all not found exceptions.
 */
public class ResourceNotFoundException extends ResourceException {

    private static final long serialVersionUID = -4447734088442483805L;

    /**
     * Constructor.
     *
     * @param message Exception message
     * @param cause reason for this exception
     */
    public ResourceNotFoundException(final String message, final Throwable cause) {
        super(HttpURLConnection.HTTP_NOT_FOUND, message, cause);
    }

    /**
     * Constructor.
     *
     * @param message Exception message
     */
    public ResourceNotFoundException(final String message) {
        super(HttpURLConnection.HTTP_NOT_FOUND, message);
    }
}
