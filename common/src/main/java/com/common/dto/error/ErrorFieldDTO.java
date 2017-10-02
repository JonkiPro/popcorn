package com.common.dto.error;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@ApiModel(description = "The object represents an erroneous field")
public class ErrorFieldDTO {

    @ApiModelProperty(notes = "The field's name", required = true)
    private String field;

    @ApiModelProperty(notes = "The error message", required = true)
    private String message;
}
