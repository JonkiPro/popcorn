package com.common.exception;

/**
 * Exception for all precondition failures.
 */
public class ResourcePreconditionException extends RuntimeException {

    /**
     * Constructor.
     *
     * @param message Exception message
     */
    public ResourcePreconditionException(final String message) {
        super(message);
    }
}
