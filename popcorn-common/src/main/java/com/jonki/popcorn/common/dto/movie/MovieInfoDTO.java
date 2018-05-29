package com.jonki.popcorn.common.dto.movie;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModel;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Common class for resources and resource creation requests. (BoxOffice, ReleaseDate, etc)
 * Common class only for classes related to movie content.
 */
@EqualsAndHashCode
@ApiModel(description = "The base object for objects containing information about movies")
public abstract class MovieInfoDTO implements Serializable {

    private static final long serialVersionUID = -5715641639272774083L;

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
