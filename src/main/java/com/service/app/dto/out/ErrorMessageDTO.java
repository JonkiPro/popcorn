package com.service.app.dto.out;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

@Data
@AllArgsConstructor
@ApiObject
public class ErrorMessageDTO {

    @ApiObjectField(description = "The field's name")
    private String field;

    @ApiObjectField(description = "The error message")
    private String message;
}
