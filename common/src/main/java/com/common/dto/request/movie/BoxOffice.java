package com.common.dto.request.movie;

import com.common.dto.movie.CountryType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Box office movie")
public class BoxOffice {

    @ApiModelProperty(notes = "Amount of money earned", required = true)
    private String boxOffice;

    @ApiModelProperty(notes = "Country of money earned")
    private CountryType country;
}
