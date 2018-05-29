package com.jonki.popcorn.common.dto.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jonki.popcorn.common.dto.CommonResource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Read only data transfer object representing a list of invalid fields in resource creation requests (DTOs).
 */
@Getter
@NoArgsConstructor
@ApiModel(description = "An object with a list of erroneous fields")
public class ValidationErrorDTO extends CommonResource {

    private static final long serialVersionUID = 7732945376507195373L;

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
        this.fieldErrors.add(error);
    }
}
