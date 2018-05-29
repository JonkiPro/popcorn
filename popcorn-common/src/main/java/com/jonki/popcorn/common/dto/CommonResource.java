package com.jonki.popcorn.common.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Common class for multiple resources (Contribution, Movie, etc).
 */
@EqualsAndHashCode
public abstract class CommonResource implements Serializable {

    private static final long serialVersionUID = 6122338577055171898L;

    /**
     * Convert this object to a string representation.
     *
     * @return This application data represented as a JSON structure
     */
    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (final JsonProcessingException ioe) {
            return ioe.getLocalizedMessage();
        }
    }
}
