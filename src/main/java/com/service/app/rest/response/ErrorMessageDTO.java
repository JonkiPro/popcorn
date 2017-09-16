package com.service.app.rest.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel
public class ErrorMessageDTO {

    @ApiModelProperty(notes = "Contains the same HTTP Status code returned by the server", required = true)
    private int status;

    @ApiModelProperty(notes = "Application specific error code", required = true)
    private int code;

    @ApiModelProperty(notes = "Message describing the error", required = true)
    private String message;
}
