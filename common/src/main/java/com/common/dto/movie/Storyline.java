package com.common.dto.movie;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

@Getter
@Builder
@ApiModel(description = "The storyline of the movie")
public class Storyline extends MovieInfoDTO {

    @ApiModelProperty(notes = "The storyline", required = true)
    @NotEmpty
    private String storyline;
}
