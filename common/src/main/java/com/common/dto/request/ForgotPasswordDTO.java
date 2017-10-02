package com.common.dto.request;

import com.common.dto.request.validator.IsValidEmail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

@Data
@ApiModel(description = "Data to reset a user password")
public class ForgotPasswordDTO {

    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9_-]{6,36}")
    @ApiModelProperty(notes = "The user's name", required = true)
    private String username;

    @NotBlank
    @IsValidEmail
    @ApiModelProperty(notes = "The user's e-mail", required = true)
    private String email;
}
