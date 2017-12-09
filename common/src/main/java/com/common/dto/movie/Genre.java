package com.common.dto.movie;

import com.common.dto.movie.type.GenreType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@ApiModel(description = "The genre of the movie")
public class Genre extends MovieInfoDTO {

    @ApiModelProperty(notes = "The genre", required = true)
    @NotNull
    private GenreType genre;
}
