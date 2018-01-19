package com.common.exception;

import java.net.HttpURLConnection;

/**
 * The data are forbidden.
 */
public class ResourceForbiddenException extends ResourceException {

    private static final long serialVersionUID = -8561926955377918834L;

    /**
     * Constructor.
     *
     * @param message Exception message
     * @param cause reason for this exception
     */
    public ResourceForbiddenException(final String message, final Throwable cause) {
        super(HttpURLConnection.HTTP_FORBIDDEN, message, cause);
    }

    /**
     * Constructor.
     *
     * @param message Exception message
     */
    public ResourceForbiddenException(final String message) {
        super(HttpURLConnection.HTTP_FORBIDDEN, message);
    }
}
