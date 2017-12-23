package com.common.dto.movie;

import com.common.dto.movie.type.CountryType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@JsonDeserialize(builder = BoxOffice.Builder.class)
@ApiModel(description = "The box office of the movie")
public class BoxOffice extends MovieInfoDTO {

    private static final long serialVersionUID = -3396095102525717768L;

    @ApiModelProperty(notes = "Amount of money earned", required = true)
    @NotNull
    private final String boxOffice;

    @ApiModelProperty(notes = "Country of money earned", required = true)
    @NotNull
    private final CountryType country;

    /**
     * Constructor only accessible via builder build() method.
     *
     * @param builder The builder to get data from
     */
    private BoxOffice(final Builder builder) {
        this.boxOffice = builder.bBoxOffice;
        this.country = builder.bCountry;
    }

    /**
     * A builder to create box offices.
     */
    public static class Builder {

        private final String bBoxOffice;
        private final CountryType bCountry;

        /**
         * Constructor which has required fields.
         *
         * @param boxOffice The box office of the movie
         * @param country The country of the box office
         */
        @JsonCreator
        public Builder(
                @JsonProperty("boxOffice") final String boxOffice,
                @JsonProperty("country") final CountryType country
        ) {
            this.bBoxOffice = boxOffice;
            this.bCountry = country;
        }

        /**
         * Build the box office.
         *
         * @return Create the final read-only BoxOffice instance
         */
        public BoxOffice build() {
            return new BoxOffice(this);
        }
    }
}
