package com.service.app.dto.out;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class UserProfileDTO {

    @ApiModelProperty(notes = "The user's ID", required = true)
    private Long id;

    @ApiModelProperty(notes = "The user's name", required = true)
    private String username;
}
