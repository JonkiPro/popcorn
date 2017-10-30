package com.common.dto.request.movie;

import com.common.dto.movie.SiteType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "The website of the movie")
public class Site {

    @ApiModelProperty(notes = "The website of the movie", required = true)
    private String site;

    @ApiModelProperty(notes = "Type of website", required = true)
    private SiteType official;
}
