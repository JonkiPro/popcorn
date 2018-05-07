package com.jonki.popcorn.common.dto.request;

import com.jonki.popcorn.common.dto.request.validator.ValidEmail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(description = "Data to recover the username")
public class ForgotUsernameDTO {

    @NotBlank
    @ValidEmail
    @ApiModelProperty(notes = "The user's e-mail", required = true)
    private String email;
}
