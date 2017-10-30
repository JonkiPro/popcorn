package com.common.dto.request.movie;

import com.common.dto.movie.CountryType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Another movie title")
public class OtherTitle {

    @ApiModelProperty(notes = "Title of the movie", required = true)
    private String title;

    @ApiModelProperty(notes = "Country of title", required = true)
    private CountryType country;
}
