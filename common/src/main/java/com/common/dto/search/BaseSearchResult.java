package com.common.dto.search;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

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
    public BaseSearchResult(
            @NotNull @JsonProperty("id") final String id
    ) {
        this.id = id;
    }
}
