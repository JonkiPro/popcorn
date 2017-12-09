package com.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
@ApiModel(description = "Message data received")
public class MessageReceived {

    @ApiModelProperty(notes = "The message ID", required = true)
    private Long id;

    @ApiModelProperty(notes = "The sender's username", required = true)
    private String sender;

    @ApiModelProperty(notes = "The message subject", required = true)
    private String subject;

    @ApiModelProperty(notes = "The message text", required = true)
    private String text;

    @ApiModelProperty(notes = "The message date of sent", required = true)
    private Date date;

    @JsonProperty("date_of_read")
    @ApiModelProperty(notes = "The message date of read")
    private Date dateOfRead;
}
