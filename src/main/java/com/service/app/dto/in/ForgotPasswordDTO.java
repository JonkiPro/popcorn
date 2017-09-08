package com.service.app.dto.in;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class ForgotPasswordDTO {

    @NotEmpty
    private String username;

    @NotEmpty
    private String email;
}
