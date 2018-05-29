package com.jonki.popcorn.common.dto.movie.request;

import com.jonki.popcorn.common.dto.request.CommonRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Fields representing all the values users can set when creating a new Rate resource.
 */
@Getter
@ApiModel(description = "Movie rating")
public class RateRequest extends CommonRequest {

    private static final long serialVersionUID = 6181912334372906796L;

    @ApiModelProperty(notes = "Movie rating", required = true)
    @NotNull
    @Min(1)
    @Max(10)
    private Integer rate;
}
