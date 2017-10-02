package com.common.exception;

/**
 * The data are bad request.
 */
public class ResourceBadRequestException extends RuntimeException {

    /**
     * Constructor.
     *
     * @param message Exception message
     */
    public ResourceBadRequestException(final String message) {
        super(message);
    }
}
