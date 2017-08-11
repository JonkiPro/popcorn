package com.service.app.dto.in;

import com.service.app.validator.annotation.ExistsUsername;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.validation.constraints.Size;

@Data
@ApiObject(group = "Message")
public class SendMessageDTO {

    @NotEmpty
    @ExistsUsername
    @ApiObjectField(description = "The recipient's username")
    private String to;

    @NotEmpty
    @Size(min = 1, max = 255)
    @ApiObjectField(description = "The message subject")
    private String subject;

    @NotEmpty
    @Size(min = 1, max = 4000)
    @ApiObjectField(description = "The message text")
    private String text;
}
