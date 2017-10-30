package com.common.dto;

import com.common.dto.movie.MovieType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@ApiModel(description = "Movie details")
public class Movie {

    @ApiModelProperty(notes = "The movie ID", required = true)
    private Long id;

    @ApiModelProperty(notes = "Title of the movie", required = true)
    private String title;

    @ApiModelProperty(notes = "Type of the movie", required = true)
    private MovieType type;
}
