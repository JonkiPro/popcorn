package com.jonki.popcorn.common.dto.error;

import com.jonki.popcorn.common.dto.CommonResource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Read only data transfer object representing an invalid field in resource creation requests (DTOs).
 */
@Getter
@AllArgsConstructor
@ApiModel(description = "The object represents an erroneous field")
public class ErrorFieldDTO extends CommonResource {

    private static final long serialVersionUID = 2515189918928185216L;

    @ApiModelProperty(notes = "The field's name", required = true)
    private String field;

    @ApiModelProperty(notes = "The error message", required = true)
    private String message;
}
