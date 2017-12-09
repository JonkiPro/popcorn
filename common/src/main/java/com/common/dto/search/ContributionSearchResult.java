package com.common.dto.search;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
@ApiModel(description = "This class represents the subset of data returned from a Contribution when a search for Contributions is conducted")
public class ContributionSearchResult {

    @ApiModelProperty(notes = "The contribution ID", required = true)
    private Long id;

    @ApiModelProperty(notes = "The movie field", required = true)
    private String field;

    @ApiModelProperty(notes = "Date of creation of the contribution", required = true)
    private Date date;
}
