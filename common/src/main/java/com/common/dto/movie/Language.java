package com.common.dto.movie;

import com.common.dto.movie.type.LanguageType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@ApiModel(description = "The language of the movie")
public class Language extends MovieInfoDTO {

    @ApiModelProperty(notes = "The language", required = true)
    @NotNull
    private LanguageType language;
}
