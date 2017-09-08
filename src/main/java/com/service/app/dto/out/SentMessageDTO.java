package com.service.app.dto.out;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class SentMessageDTO {

    @ApiModelProperty(notes = "The message ID", required = true)
    private Long id;

    @ApiModelProperty(notes = "The recipient's username", required = true)
    private String recipient;

    @ApiModelProperty(notes = "The message subject", required = true)
    private String subject;

    @ApiModelProperty(notes = "The message text", required = true)
    private String text;
}
