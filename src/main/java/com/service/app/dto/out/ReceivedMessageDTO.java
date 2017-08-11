package com.service.app.dto.out;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import java.util.Date;

@Data
@ApiObject(group = "Message")
public class ReceivedMessageDTO {

    @ApiObjectField(description = "The message ID")
    private Long id;

    @ApiObjectField(description = "The sender's username")
    private String sender;

    @ApiObjectField(description = "The message subject")
    private String subject;

    @ApiObjectField(description = "The message text")
    private String text;

    @JsonProperty("date_of_read")
    @ApiObjectField(name = "date_of_read", description = "The message date of read")
    private Date dateOfRead;
}
