package com.common.dto.movie.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@ApiModel(description = "Movie rating")
public class Rate {

    @ApiModelProperty(notes = "Movie rating", required = true)
    @NotNull
    @Min(1)
    @Max(10)
    private Integer rate;
}
