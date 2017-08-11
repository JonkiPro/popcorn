package com.service.app.dto.out;

import lombok.Data;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

@Data
@ApiObject(group = "User")
public class UserInfoDTO {

    @ApiObjectField(description = "The user's name")
    private String username;
}
