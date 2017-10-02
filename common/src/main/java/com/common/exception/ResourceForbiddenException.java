package com.common.exception;

/**
 * The data are forbidden.
 */
public class ResourceForbiddenException extends RuntimeException {

    /**
     * Constructor.
     *
     * @param message Exception message
     */
    public ResourceForbiddenException(final String message) {
        super(message);
    }
}
