package com.service.app.rest.request;

import com.service.app.validator.annotation.ExistsEmail;
import com.service.app.validator.annotation.ExistsUsername;
import com.service.app.validator.annotation.IsValidEmail;
import com.service.app.validator.annotation.ReCaptcha;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

@Data
@ApiModel
public class RegisterDTO {

    @NotEmpty
    @Pattern(regexp = "[a-zA-Z0-9_-]{6,36}")
    @ExistsUsername(ifExistsReturn = false)
    @ApiModelProperty(notes = "The user's name", required = true)
    private String username;

    @NotEmpty
    @IsValidEmail
    @ExistsEmail(ifExistsReturn = false)
    @ApiModelProperty(notes = "The user's e-mail", required = true)
    private String email;

    @NotEmpty
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9])(?=\\S+$).{6,36}$")
    @ApiModelProperty(notes = "The user's password", required = true)
    private String password;

    @NotEmpty
    @ApiModelProperty(notes = "The user's password again", required = true)
    private String passwordAgain;

    @ReCaptcha
    @ApiModelProperty(notes = "ReCaptcha response", required = true)
    private String reCaptcha;
}