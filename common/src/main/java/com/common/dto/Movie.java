package com.common.dto;

import com.common.dto.movie.type.MovieType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
@ApiModel(description = "Movie details")
public class Movie {

    @ApiModelProperty(notes = "The movie ID", required = true)
    private Long id;

    @ApiModelProperty(notes = "The title of the movie", required = true)
    private String title;

    @ApiModelProperty(notes = "The type of the movie", required = true)
    private MovieType type;
}
