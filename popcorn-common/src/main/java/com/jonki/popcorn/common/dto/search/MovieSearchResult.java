package com.jonki.popcorn.common.dto.search;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jonki.popcorn.common.dto.movie.type.MovieType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * This class represents the subset of data returned from a Movie when a search for Movies is conducted.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "This class represents the subset of data returned from a Movie when a search for Movies is conducted")
public class MovieSearchResult extends BaseSearchResult {

    private static final long serialVersionUID = 7523781849986382925L;

    @ApiModelProperty(notes = "The title of the movie", required = true)
    private final String title;

    @ApiModelProperty(notes = "The type of the movie", required = true)
    private final MovieType type;

    @ApiModelProperty(notes = "Movie rating", required = true)
    private final Float rating;

    /**
     * Constructor.
     *
     * @param id The movie ID
     * @param title The title of the movie
     * @param type The type of the movie
     * @param rating The movie rating
     */
    @JsonCreator
    public MovieSearchResult(
            @NotNull @JsonProperty("id") final Long id,
            @NotBlank @JsonProperty("title") final String title,
            @NotNull @JsonProperty("type") final MovieType type,
            @NotNull @JsonProperty("rating") final Float rating
    ) {
        super(id.toString());
        this.title = title;
        this.type = type;
        this.rating = rating;
    }
}
