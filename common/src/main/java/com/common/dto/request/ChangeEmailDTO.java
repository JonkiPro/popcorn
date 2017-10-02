package com.common.dto.request;

import com.common.dto.request.validator.IsValidEmail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

@Data
@ApiModel(description = "Data to change user address email")
public class ChangeEmailDTO {

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9])(?=\\S+$).{6,36}$")
    @ApiModelProperty(notes = "The user's password", required = true)
    private String password;

    @NotBlank
    @IsValidEmail
    @ApiModelProperty(notes = "The user's new e-mail", required = true)
    private String email;
}
