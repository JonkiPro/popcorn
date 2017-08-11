package com.service.app.dto.in;

import com.service.app.validator.annotation.ExistsEmail;
import com.service.app.validator.annotation.ExistsUsername;
import com.service.app.validator.annotation.IsValidEmail;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.validation.constraints.Pattern;

@Data
@ApiObject
public class RegisterDTO {

    @NotEmpty
    @Pattern(regexp = "[a-zA-Z0-9_-]{6,36}")
    @ExistsUsername(ifExistsReturn = false)
    @ApiObjectField(description = "The user's name")
    private String username;

    @NotEmpty
    @IsValidEmail
    @ExistsEmail(ifExistsReturn = false)
    @ApiObjectField(description = "The user's e-mail")
    private String email;

    @NotEmpty
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9])(?=\\S+$).{6,36}$")
    @ApiObjectField(description = "The user's password")
    private String password;

    @NotEmpty
    @ApiObjectField(name = "password_again", description = "Again the user's password")
    private String passwordAgain;

    @ApiObjectField(name = "activation_token", description = "Automatically generated token to activate your account")
    private String activationToken;
}