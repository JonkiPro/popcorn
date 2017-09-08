package com.service.app.dto.in;

import com.service.app.validator.annotation.ExistsEmail;
import com.service.app.validator.annotation.IsValidEmail;
import com.service.app.validator.annotation.PasswordIsTheSame;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@ApiModel
public class ChangeEmailDTO {

    @NotEmpty
    @PasswordIsTheSame
    @ApiModelProperty(notes = "The user's password", required = true)
    private String password;

    @NotEmpty
    @IsValidEmail
    @ExistsEmail(ifExistsReturn = false)
    @ApiModelProperty(notes = "The user's new email", required = true)
    private String email;
}
