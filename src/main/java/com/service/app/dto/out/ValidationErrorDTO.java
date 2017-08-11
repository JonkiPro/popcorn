package com.service.app.dto.out;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@ApiObject
public class ValidationErrorDTO {

    @JsonProperty("field_errors")
    @ApiObjectField(description = "The list of errors")
    private List<ErrorMessageDTO> fieldErrors = new ArrayList<>();

    public void addFieldError(String path, String message) {
        ErrorMessageDTO error = new ErrorMessageDTO(path, message);
        fieldErrors.add(error);
    }
}
