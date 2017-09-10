package com.service.app.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel
public class ReceivedMessageDTO {

    @ApiModelProperty(notes = "The message ID", required = true)
    private Long id;

    @ApiModelProperty(notes = "The sender's username", required = true)
    private String sender;

    @ApiModelProperty(notes = "The message subject", required = true)
    private String subject;

    @ApiModelProperty(notes = "The message text", required = true)
    private String text;

    @JsonProperty("date_of_read")
    @ApiModelProperty(notes = "The message date of read")
    private Date dateOfRead;
}
