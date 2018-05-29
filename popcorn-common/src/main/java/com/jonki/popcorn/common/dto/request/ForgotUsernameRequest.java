package com.jonki.popcorn.common.dto.request;

import com.jonki.popcorn.common.dto.request.validator.ValidEmail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

/**
 * Fields representing all the values users can set when creating a ForgotPasswordRequest DTO to recover the username.
 */
@Getter
@ApiModel(description = "Data to recover the username")
public class ForgotUsernameRequest extends CommonRequest {

    private static final long serialVersionUID = 6640634320194977663L;

    @NotBlank
    @ValidEmail
    @ApiModelProperty(notes = "The user's e-mail", required = true)
    private String email;
}
