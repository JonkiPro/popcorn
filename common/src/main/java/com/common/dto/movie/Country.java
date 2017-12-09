package com.common.dto.movie;

import com.common.dto.movie.type.CountryType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@ApiModel(description = "The country of the movie")
public class Country extends MovieInfoDTO {

    @ApiModelProperty(notes = "The country", required = true)
    @NotNull
    private CountryType country;
}
