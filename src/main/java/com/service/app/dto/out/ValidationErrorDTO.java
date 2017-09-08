package com.service.app.dto.out;

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
    private List<ErrorMessageDTO> fieldErrors = new ArrayList<>();

    public void addFieldError(String path, String message) {
        ErrorMessageDTO error = new ErrorMessageDTO(path, message);
        fieldErrors.add(error);
    }
}
