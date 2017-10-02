package com.common.exception;

/**
 * The data are conflicting.
 */
public class ResourceConflictException extends RuntimeException {

    /**
     * Constructor.
     *
     * @param message Exception message
     */
    public ResourceConflictException(final String message) {
        super(message);
    }
}
