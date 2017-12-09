package com.common.dto.movie;

import com.common.dto.movie.type.SiteType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@ApiModel(description = "The website of the movie")
public class Site extends MovieInfoDTO {

    @ApiModelProperty(notes = "The website", required = true)
    @NotEmpty
    @URL
    private String site;

    @ApiModelProperty(notes = "Type of website", required = true)
    @NotNull
    private SiteType official;
}
