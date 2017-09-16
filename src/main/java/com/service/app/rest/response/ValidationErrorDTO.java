package com.service.app.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@ApiModel
public class ValidationErrorDTO {

    @JsonProperty("field_errors")
    @ApiModelProperty(notes = "The list of errors")
    private List<ErrorFieldDTO> fieldErrors = new ArrayList<>();

    public void addFieldError(String path, String message) {
        ErrorFieldDTO error = new ErrorFieldDTO(path, message);
        fieldErrors.add(error);
    }
}
