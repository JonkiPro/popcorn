package com.service.app.rest.request;

import com.service.app.validator.annotation.ExistsEmail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@ApiModel
public class ForgotUsernameDTO {

    @NotEmpty
    @ExistsEmail
    @ApiModelProperty(notes = "The user's e-mail", required = true)
    private String email;
}
