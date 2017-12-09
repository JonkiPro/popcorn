package com.common.dto.movie;

import com.common.dto.movie.type.CountryType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Builder
@ApiModel(description = "The release date of the movie")
public class ReleaseDate extends MovieInfoDTO {

    @ApiModelProperty(notes = "The release date", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private Date date;

    @ApiModelProperty(notes = "The country of release date", required = true)
    @NotNull
    private CountryType country;
}
