package com.common.dto.movie;

import com.common.dto.movie.type.CountryType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@ApiModel(description = "The title of the movie")
public class OtherTitle extends MovieInfoDTO {

    @ApiModelProperty(notes = "The title", required = true)
    @NotEmpty
    private String title;

    @ApiModelProperty(notes = "The country of title", required = true)
    @NotNull
    private CountryType country;
}
