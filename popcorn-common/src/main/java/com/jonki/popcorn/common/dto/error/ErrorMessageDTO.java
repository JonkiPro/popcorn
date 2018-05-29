package com.jonki.popcorn.common.dto.error;

import com.jonki.popcorn.common.dto.CommonResource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Read only data transfer object representing an error message from exceptions.
 */
@Getter
@Builder
@AllArgsConstructor
@ApiModel(description = "Error status message")
public class ErrorMessageDTO extends CommonResource {

    private static final long serialVersionUID = -7748625839878549168L;

    @ApiModelProperty(notes = "Contains the same HTTP Status code returned by the server", required = true)
    private int status;

    @ApiModelProperty(notes = "Application specific error code", required = true)
    private int code;

    @ApiModelProperty(notes = "Message describing the error", required = true)
    private String message;
}
