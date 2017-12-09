package com.common.dto.search;

import com.common.dto.movie.type.MovieType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@ApiModel(description = "This class represents the subset of data returned from a Movie when a search for Movies is conducted")
public class MovieSearchResult {

    @ApiModelProperty(notes = "The movie ID", required = true)
    private Long id;

    @ApiModelProperty(notes = "The title of the movie", required = true)
    private String title;

    @ApiModelProperty(notes = "The type of the movie", required = true)
    private MovieType type;

    @ApiModelProperty(notes = "Movie rating", required = true)
    private Float rating;
}
