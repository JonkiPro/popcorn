package com.jonki.popcorn.common.dto.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@ApiModel(description = "An object with a list of erroneous fields")
public class ValidationErrorDTO {

    @JsonProperty("field_errors")
    @ApiModelProperty(notes = "The list of errors")
    private List<ErrorFieldDTO> fieldErrors = new ArrayList<>();

    /**
     * Add an erroneous field to the list.
     *
     * @param path Field name
     * @param message Error message
     */
    public void addFieldError(final String path, final String message) {
        final ErrorFieldDTO error = new ErrorFieldDTO(path, message);
        fieldErrors.add(error);
    }
}
