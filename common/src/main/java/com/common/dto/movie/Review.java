package com.common.dto.movie;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

@Getter
@Builder
@ApiModel(description = "The review of the movie")
public class Review extends MovieInfoDTO {

    @ApiModelProperty(notes = "The title", required = true)
    @NotEmpty
    @Size(max = 64)
    private String title;

    @ApiModelProperty(notes = "The review", required = true)
    @NotEmpty
    private String review;
}
