package com.service.app.dto.out;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

@Data
@AllArgsConstructor
@ApiObject
public class RelationshipStatusDTO {

    @ApiObjectField(description = "The relation status")
    private String status;
}
