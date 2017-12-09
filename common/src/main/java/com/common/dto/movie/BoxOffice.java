package com.common.dto.movie;

import com.common.dto.movie.type.CountryType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@ApiModel(description = "The box office of the movie")
public class BoxOffice extends MovieInfoDTO {

    @ApiModelProperty(notes = "Amount of money earned", required = true)
    @NotNull
    private String boxOffice;

    @ApiModelProperty(notes = "Country of money earned", required = true)
    @NotNull
    private CountryType country;
}
