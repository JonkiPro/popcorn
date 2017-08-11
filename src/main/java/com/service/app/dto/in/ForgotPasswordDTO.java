package com.service.app.dto.in;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

@Data
@ApiObject
public class ForgotPasswordDTO {

    @NotEmpty
    @ApiObjectField(description = "The user's name")
    private String username;

    @NotEmpty
    @ApiObjectField(description = "The user's e-mail")
    private String email;
}
