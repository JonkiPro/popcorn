package com.jonki.popcorn.common.dto.request;

import com.jonki.popcorn.common.dto.request.validator.ValidEmail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * Fields representing all the values users can set when creating a ChangeEmailRequest DTO to change the e-mail.
 */
@Getter
@ApiModel(description = "Data to change user address email")
public class ChangeEmailRequest extends CommonRequest {

    private static final long serialVersionUID = 4997364125867153959L;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9])(?=\\S+$).{6,36}$")
    @ApiModelProperty(notes = "The user's password", required = true)
    private String password;

    @NotBlank
    @ValidEmail
    @ApiModelProperty(notes = "The user's new e-mail", required = true)
    private String email;
}
