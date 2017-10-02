package com.common.exception;

import com.common.dto.error.ValidationErrorDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

/**
 * The DTO object contains errors that could not be checked with standard validators in the annotations.
 */
@AllArgsConstructor
public class FormBadRequestException extends RuntimeException {

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
        return fieldErrors;
    }
}
