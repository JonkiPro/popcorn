package com.jonki.popcorn.common.exception;

import java.net.HttpURLConnection;

/**
 * Exception for all internal server failures.
 */
public class ResourceServerException extends ResourceException {

    private static final long serialVersionUID = 7309086831414947253L;

    /**
     * Constructor.
     *
     * @param message Exception message
     * @param cause reason for this exception
     */
    public ResourceServerException(final String message, final Throwable cause) {
        super(HttpURLConnection.HTTP_INTERNAL_ERROR, message, cause);
    }

    /**
     * Constructor.
     *
     * @param message Exception message
     */
    public ResourceServerException(final String message) {
        super(HttpURLConnection.HTTP_INTERNAL_ERROR, message);
    }
}
