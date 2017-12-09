package com.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@ApiModel(description = "User data")
public class User {

    @ApiModelProperty(notes = "The user ID", required = true)
    private Long id;

    @ApiModelProperty(notes = "The user's name", required = true)
    private String username;

    @ApiModelProperty(notes = "The user's e-mail", required = true)
    private String email;
}
