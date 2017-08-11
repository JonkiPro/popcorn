package com.service.app.dto.out;

import lombok.Data;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

@Data
@ApiObject(group = "Message")
public class SentMessageDTO {

    @ApiObjectField(description = "The message ID")
    private Long id;

    @ApiObjectField(description = "The recipient's username")
    private String recipient;

    @ApiObjectField(description = "The message subject")
    private String subject;

    @ApiObjectField(description = "The message text")
    private String text;
}
