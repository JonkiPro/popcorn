package com.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@ApiModel(description = "Message data sent")
public class MessageSent {

    @ApiModelProperty(notes = "The message ID", required = true)
    private Long id;

    @ApiModelProperty(notes = "The recipient's username", required = true)
    private String recipient;

    @ApiModelProperty(notes = "The message subject", required = true)
    private String subject;

    @ApiModelProperty(notes = "The message text", required = true)
    private String text;

    @ApiModelProperty(notes = "The message date of sent", required = true)
    private Date date;
}
