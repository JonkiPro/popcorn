package com.jonki.popcorn.common.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jonki.popcorn.common.dto.error.ValidationErrorDTO;
import lombok.AllArgsConstructor;

/**
 * The DTO object contains errors that could not be checked with standard validators in the annotations.
 */
@AllArgsConstructor
public class FormBadRequestException extends RuntimeException {

    private static final long serialVersionUID = -7344751131503160217L;

    /**
     * An object with a list of erroneous fields.
     */
    @JsonProperty("field_errors")
    private ValidationErrorDTO fieldErrors;

    /**
     * Get an object with a list of erroneous fields.
     *
     * @return Object with error list
     */
    public ValidationErrorDTO getErrors() {
        return this.fieldErrors;
    }
}
