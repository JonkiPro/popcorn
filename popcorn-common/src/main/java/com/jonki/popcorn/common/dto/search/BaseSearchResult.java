package com.jonki.popcorn.common.dto.search;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Base class for search results containing common fields.
 */
@Getter
@EqualsAndHashCode(of = "id")
@ApiModel(description = "The base object for search results")
public class BaseSearchResult implements Serializable {

    private static final long serialVersionUID = -3657881229751471346L;

    @ApiModelProperty(notes = "The ID", required = true)
    private final String id;

    /**
     * Constructor.
     *
     * @param id The id of the object in the search result
     */
    @JsonCreator
    BaseSearchResult(
            @NotNull @JsonProperty("id") final String id
    ) {
        this.id = id;
    }

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
