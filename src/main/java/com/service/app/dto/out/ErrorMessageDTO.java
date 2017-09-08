package com.service.app.dto.out;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@ApiModel
public class ErrorMessageDTO {

    @ApiModelProperty(notes = "The field's name", required = true)
    private String field;

    @ApiModelProperty(notes = "The error message", required = true)
    private String message;
}
