package com.service.app.dto.out;

import lombok.Data;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import java.util.Date;

@Data
@ApiObject(group = "Message")
public class ReceivedMessageInfoDTO {

    @ApiObjectField(description = "The message ID")
    private Long id;

    @ApiObjectField(description = "The sender's username")
    private String sender;

    @ApiObjectField(description = "The message subject")
    private String subject;

    @ApiObjectField(description = "The message text")
    private String text;

    @ApiObjectField(description = "The message date of sent")
    private Date date;
}
