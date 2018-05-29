package com.jonki.popcorn.common.dto.movie;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jonki.popcorn.common.dto.movie.type.LanguageType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotNull;

/**
 * Language DTO. The class serves both as a resource and a resource creation request.
 */
@Getter
@JsonDeserialize(builder = Language.Builder.class)
@ApiModel(description = "The language of the movie")
public class Language extends MovieInfoDTO {

    private static final long serialVersionUID = 7168079991357479562L;

    @ApiModelProperty(notes = "The language", required = true)
    @NotNull
    private final LanguageType language;

    /**
     * Constructor only accessible via builder build() method.
     *
     * @param builder The builder to get data from
     */
    private Language(final Builder builder) {
        this.language = builder.bLanguage;
    }

    /**
     * A builder to create languages.
     */
    public static class Builder {

        private final LanguageType bLanguage;

        /**
         * Constructor which has required fields.
         *
         * @param language The language of the movie
         */
        @JsonCreator
        public Builder(
                @JsonProperty("language") final LanguageType language
        ) {
            this.bLanguage = language;
        }

        /**
         * Build the language.
         *
         * @return Create the final read-only Language instance
         */
        public Language build() {
            return new Language(this);
        }
    }
}
