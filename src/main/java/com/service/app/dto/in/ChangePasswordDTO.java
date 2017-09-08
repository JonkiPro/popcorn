package com.service.app.dto.in;

import com.service.app.validator.annotation.PasswordIsDifferent;
import com.service.app.validator.annotation.PasswordIsTheSame;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

@Data
@ApiModel
public class ChangePasswordDTO {

    @NotEmpty
    @PasswordIsTheSame
    @ApiModelProperty(notes = "The user's password", required = true)
    private String oldPassword;

    @NotEmpty
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9])(?=\\S+$).{6,36}$")
    @PasswordIsDifferent
    @ApiModelProperty(notes = "The user's new password", required = true)
    private String newPassword;

    @NotEmpty
    @ApiModelProperty(notes = "Again the user's new password", required = true)
    private String newPasswordAgain;
}
