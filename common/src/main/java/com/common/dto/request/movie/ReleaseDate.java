package com.common.dto.request.movie;

import com.common.dto.movie.CountryType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "The release date of the movie")
public class ReleaseDate {

    @ApiModelProperty(notes = "The release date of the movie", required = true)
    private Date releaseDate;

    @ApiModelProperty(notes = "Country of release date", required = true)
    private CountryType country;
}
