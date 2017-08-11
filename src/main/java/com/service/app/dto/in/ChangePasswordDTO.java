package com.service.app.dto.in;

import com.service.app.validator.annotation.PasswordIsDifferent;
import com.service.app.validator.annotation.PasswordIsTheSame;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.validation.constraints.Pattern;

@Data
@ApiObject(group = "Settings")
public class ChangePasswordDTO {

    @NotEmpty
    @PasswordIsTheSame
    @ApiObjectField(name = "old_password", description = "The user's password")
    private String oldPassword;

    @NotEmpty
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9])(?=\\S+$).{6,36}$")
    @PasswordIsDifferent
    @ApiObjectField(name = "new_password", description = "The user's new password")
    private String newPassword;

    @NotEmpty
    @ApiObjectField(name = "new_password_again", description = "Again the user's new password")
    private String newPasswordAgain;
}
