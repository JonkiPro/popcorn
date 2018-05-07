package com.jonki.popcorn.common.exception;

import java.net.HttpURLConnection;

/**
 * Exception for all bad request failures.
 */
public class ResourceBadRequestException extends ResourceException {

    private static final long serialVersionUID = 3192881824877596290L;

    /**
     * Constructor.
     *
     * @param message Exception message
     * @param cause reason for this exception
     */
    public ResourceBadRequestException(final String message, final Throwable cause) {
        super(HttpURLConnection.HTTP_BAD_REQUEST, message, cause);
    }

    /**
     * Constructor.
     *
     * @param message Exception message
     */
    public ResourceBadRequestException(final String message) {
        super(HttpURLConnection.HTTP_BAD_REQUEST, message);
    }
}
