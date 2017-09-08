package com.service.app.dto.in;

import com.service.app.validator.annotation.ExistsEmail;
import com.service.app.validator.annotation.ExistsUsername;
import com.service.app.validator.annotation.IsValidEmail;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

@Data
public class RegisterDTO {

    @NotEmpty
    @Pattern(regexp = "[a-zA-Z0-9_-]{6,36}")
    @ExistsUsername(ifExistsReturn = false)
    private String username;

    @NotEmpty
    @IsValidEmail
    @ExistsEmail(ifExistsReturn = false)
    private String email;

    @NotEmpty
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9])(?=\\S+$).{6,36}$")
    private String password;

    @NotEmpty
    private String passwordAgain;

    private String activationToken;
}