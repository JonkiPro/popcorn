package com.service.app.dto.in;

import com.service.app.validator.annotation.ExistsEmail;
import com.service.app.validator.annotation.IsValidEmail;
import com.service.app.validator.annotation.PasswordIsTheSame;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

@Data
@ApiObject(group = "Settings")
public class ChangeEmailDTO {

    @NotEmpty
    @PasswordIsTheSame
    @ApiObjectField(description = "The user's password")
    private String password;

    @NotEmpty
    @IsValidEmail
    @ExistsEmail(ifExistsReturn = false)
    @ApiObjectField(description = "The user's new email")
    private String email;
}
