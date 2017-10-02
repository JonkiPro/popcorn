package com.common.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

@Data
@ApiModel(description = "New message data")
public class SendMessageDTO {

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
