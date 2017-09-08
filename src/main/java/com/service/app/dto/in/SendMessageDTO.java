package com.service.app.dto.in;

import com.service.app.validator.annotation.ExistsUsername;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

@Data
@ApiModel
public class SendMessageDTO {

    @NotEmpty
    @ExistsUsername
    @ApiModelProperty(notes = "The recipient's username", required = true)
    private String to;

    @NotEmpty
    @Size(min = 1, max = 255)
    @ApiModelProperty(notes = "The message subject", required = true)
    private String subject;

    @NotEmpty
    @Size(min = 1, max = 4000)
    @ApiModelProperty(notes = "The message text", required = true)
    private String text;
}
