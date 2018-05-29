package com.jonki.popcorn.common.dto.request;

import com.jonki.popcorn.common.dto.movie.type.MovieType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Fields representing all the values users can set when creating a new Movie resource.
 */
@Getter
@ApiModel(description = "Data on the new movie")
public class MovieRequest extends CommonRequest {

    private static final long serialVersionUID = -1596297267694643241L;

    @NotBlank
    @ApiModelProperty(notes = "Title of the movie", required = true)
    private String title;

    @NotNull
    @ApiModelProperty(notes = "Type of movie", required = true)
    private MovieType type;
}
