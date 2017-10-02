package com.common.exception;

/**
 * The data was not found.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructor.
     *
     * @param message Exception message
     */
    public ResourceNotFoundException(final String message) {
        super(message);
    }
}
