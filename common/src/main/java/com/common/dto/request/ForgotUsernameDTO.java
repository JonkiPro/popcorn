package com.common.dto.request;

import com.common.dto.request.validator.IsValidEmail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
@ApiModel(description = "Data to recover the username")
public class ForgotUsernameDTO {

    @NotBlank
    @IsValidEmail
    @ApiModelProperty(notes = "The user's e-mail", required = true)
    private String email;
}
