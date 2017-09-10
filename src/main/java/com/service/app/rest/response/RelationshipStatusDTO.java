package com.service.app.rest.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@ApiModel
public class RelationshipStatusDTO {

    @ApiModelProperty(notes = "The relation status", required = true)
    private String status;
}
