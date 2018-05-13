package com.jonki.popcorn.common.dto.movie;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jonki.popcorn.common.dto.TitleAttribute;
import com.jonki.popcorn.common.dto.movie.type.CountryType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.annotation.Nullable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@JsonDeserialize(builder = OtherTitle.Builder.class)
@ApiModel(description = "The title of the movie")
public class OtherTitle extends MovieInfoDTO {

    private static final long serialVersionUID = 3237450458782674885L;

    @ApiModelProperty(notes = "The title", required = true)
    @NotEmpty
    private final String title;

    @ApiModelProperty(notes = "The country of title", required = true)
    @NotNull
    private final CountryType country;

    @ApiModelProperty(notes = "Title attribute", required = true)
    private final TitleAttribute attribute;

    /**
     * Constructor only accessible via builder build() method.
     *
     * @param builder The builder to get data from
     */
    private OtherTitle(final Builder builder) {
        this.title = builder.bTitle;
        this.country = builder.bCountry;
        this.attribute = builder.bAttribute;
    }

    /**
     * A builder to create other titles.
     */
    public static class Builder {

        private final String bTitle;
        private final CountryType bCountry;
        private TitleAttribute bAttribute;

        /**
         * Constructor which has required fields.
         *
         * @param title The title of the movie
         * @param country The country of the title
         */
        @JsonCreator
        public Builder(
                @JsonProperty("title") final String title,
                @JsonProperty("country") final CountryType country
        ) {
            this.bTitle = title;
            this.bCountry = country;
        }

        /**
         * Set attribute of the title.
         *
         * @param attribute Title attribute
         * @return The builder
         */
        public Builder withAttribute(@Nullable final TitleAttribute attribute) {
            this.bAttribute = attribute;
            return this;
        }

        /**
         * Build the other title.
         *
         * @return Create the final read-only OtherTitle instance
         */
        public OtherTitle build() {
            return new OtherTitle(this);
        }
    }
}
