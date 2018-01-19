package com.common.exception;

import java.net.HttpURLConnection;

/**
 * Exception for all precondition failures.
 */
public class ResourcePreconditionException extends ResourceException {

    private static final long serialVersionUID = -2094568717038554332L;

    /**
     * Constructor.
     *
     * @param message Exception message
     * @param cause reason for this exception
     */
    public ResourcePreconditionException(final String message, final Throwable cause) {
        super(HttpURLConnection.HTTP_PRECON_FAILED, message, cause);
    }

    /**
     * Constructor.
     *
     * @param message Exception message
     */
    public ResourcePreconditionException(final String message) {
        super(HttpURLConnection.HTTP_PRECON_FAILED, message);
    }
}
