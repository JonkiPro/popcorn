package com.service.app.dto.in;

import com.service.app.validator.annotation.ExistsEmail;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class ForgotUsernameDTO {

    @NotEmpty
    @ExistsEmail
    private String email;
}
