package com.jonki.popcorn.common.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Fields representing all the values users can set when creating a new Message resource.
 */
@Getter
@ApiModel(description = "New message data")
public class MessageRequest extends CommonRequest {

    private static final long serialVersionUID = 1315035334566054222L;

    @NotBlank
    @ApiModelProperty(notes = "The recipient's username", required = true)
    private String to;

    @NotBlank
    @Size(min = 1, max = 255)
    @ApiModelProperty(notes = "The message subject", required = true)
    private String subject;

    @NotBlank
    @Size(min = 1, max = 4000)
    @ApiModelProperty(notes = "The message text", required = true)
    private String text;
}
