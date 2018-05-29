package com.jonki.popcorn.common.dto.movie;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jonki.popcorn.common.dto.movie.type.CountryType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotNull;

/**
 * Country DTO. The class serves both as a resource and a resource creation request.
 */
@Getter
@JsonDeserialize(builder = Country.Builder.class)
@ApiModel(description = "The country of the movie")
public class Country extends MovieInfoDTO {

    private static final long serialVersionUID = -2281210560676185894L;

    @ApiModelProperty(notes = "The country", required = true)
    @NotNull
    private final CountryType country;

    /**
     * Constructor only accessible via builder build() method.
     *
     * @param builder The builder to get data from
     */
    private Country(final Builder builder) {
        this.country = builder.bCountry;
    }

    /**
     * A builder to create countries.
     */
    public static class Builder {

        private final CountryType bCountry;

        /**
         * Constructor which has required fields.
         *
         * @param country The country of the movie
         */
        @JsonCreator
        public Builder(
                @JsonProperty("country") final CountryType country
        ) {
            this.bCountry = country;
        }

        /**
         * Build the country.
         *
         * @return Create the final read-only Country instance
         */
        public Country build() {
            return new Country(this);
        }
    }
}
