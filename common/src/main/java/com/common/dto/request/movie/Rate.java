package com.common.dto.request.movie;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@ApiModel(description = "Movie rating")
public class Rate {

    @Min(1)
    @Max(10)
    @ApiModelProperty(notes = "Movie rating", required = true)
    private Integer rate;
}
