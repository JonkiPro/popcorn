package com.jonki.popcorn.common.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * Fields representing all the values users can set when creating a ChangePasswordRequest DTO to change the password.
 */
@Getter
@ApiModel(description = "Data to change user password")
public class ChangePasswordRequest extends CommonRequest {

    private static final long serialVersionUID = 9059041954958259279L;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9])(?=\\S+$).{6,36}$")
    @ApiModelProperty(notes = "The user's password", required = true)
    private String oldPassword;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9])(?=\\S+$).{6,36}$")
    @ApiModelProperty(notes = "The user's new password", required = true)
    private String newPassword;

    @NotBlank
    @ApiModelProperty(notes = "Again the user's new password", required = true)
    private String newPasswordAgain;
}
