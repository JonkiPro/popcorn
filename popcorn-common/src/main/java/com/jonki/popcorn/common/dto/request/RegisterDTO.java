package com.jonki.popcorn.common.dto.request;

import com.jonki.popcorn.common.dto.request.validator.ValidEmail;
import com.jonki.popcorn.common.dto.request.validator.PasswordsEqualConstraint;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@PasswordsEqualConstraint
@ApiModel(description = "User registration data")
public class RegisterDTO {

    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9_-]{6,36}")
    @ApiModelProperty(notes = "The user's name", required = true)
    private String username;

    @NotBlank
    @ValidEmail
    @ApiModelProperty(notes = "The user's e-mail", required = true)
    private String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9])(?=\\S+$).{6,36}$")
    @ApiModelProperty(notes = "The user's password", required = true)
    private String password;

    @NotBlank
    @ApiModelProperty(notes = "The user's password again", required = true)
    private String passwordAgain;

    @ApiModelProperty(notes = "ReCaptcha response", required = true)
    private String reCaptcha;
}