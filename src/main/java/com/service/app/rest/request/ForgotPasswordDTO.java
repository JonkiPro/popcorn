package com.service.app.rest.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@ApiModel
public class ForgotPasswordDTO {

    @NotEmpty
    @ApiModelProperty(notes = "The user's name", required = true)
    private String username;

    @NotEmpty
    @ApiModelProperty(notes = "The user's e-mail", required = true)
    private String email;
}
