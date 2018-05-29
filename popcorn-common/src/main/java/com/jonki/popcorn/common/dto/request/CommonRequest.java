package com.jonki.popcorn.common.dto.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Common class for multiple resource creation requests (ContributionNewRequest, MovieRequest, etc).
 */
@EqualsAndHashCode
public abstract class CommonRequest implements Serializable {

    private static final long serialVersionUID = 3413739347424139684L;

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
