package com.common.exception;

import java.net.HttpURLConnection;

/**
 * Exception for all bad conflict failures.
 */
public class ResourceConflictException extends ResourceException {

    private static final long serialVersionUID = -2114197262691273420L;

    /**
     * Constructor.
     *
     * @param message Exception message
     * @param cause reason for this exception
     */
    public ResourceConflictException(final String message, final Throwable cause) {
        super(HttpURLConnection.HTTP_CONFLICT, message, cause);
    }

    /**
     * Constructor.
     *
     * @param message Exception message
     */
    public ResourceConflictException(final String message) {
        super(HttpURLConnection.HTTP_CONFLICT, message);
    }
}
