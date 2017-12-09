package com.common.dto.movie.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@ApiModel(description = "Movie rating")
public class RateResponse {

    @ApiModelProperty(notes = "Movie rating", required = true)
    private Integer rate;

    @ApiModelProperty(notes = "The user's name", required = true)
    private String user;

    @ApiModelProperty(notes = "Date of rating", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date date;
}
