package com.service.app.dto.in;

import com.service.app.validator.annotation.ExistsEmail;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

@Data
@ApiObject
public class ForgotUsernameDTO {

    @NotEmpty
    @ExistsEmail
    @ApiObjectField(description = "The user's e-mail")
    private String email;
}
