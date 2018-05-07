package com.jonki.popcorn.common.dto.request;

import com.jonki.popcorn.common.dto.movie.type.MovieType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "Data on the new movie")
public class MovieDTO {

    @NotBlank
    @ApiModelProperty(notes = "Title of the movie", required = true)
    private String title;

    @NotNull
    @ApiModelProperty(notes = "Type of movie", required = true)
    private MovieType type;
}
