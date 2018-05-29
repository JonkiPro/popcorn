package com.jonki.popcorn.common.dto.movie;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jonki.popcorn.common.dto.movie.type.CountryType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * ReleaseDate DTO. The class serves both as a resource and a resource creation request.
 */
@Getter
@JsonDeserialize(builder = ReleaseDate.Builder.class)
@ApiModel(description = "The release date of the movie")
public class ReleaseDate extends MovieInfoDTO {

    private static final long serialVersionUID = 1525690153875720181L;

    @ApiModelProperty(notes = "The release date", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private final Date date;

    @ApiModelProperty(notes = "The country of release date", required = true)
    @NotNull
    private final CountryType country;

    /**
     * Constructor only accessible via builder build() method.
     *
     * @param builder The builder to get data from
     */
    private ReleaseDate(final Builder builder) {
        this.date = builder.bDate;
        this.country = builder.bCountry;
    }

    /**
     * A builder to create release dates.
     */
    public static class Builder {

        private final Date bDate;
        private final CountryType bCountry;

        /**
         * Constructor which has required fields.
         *
         * @param date The release date of the movie
         * @param country The country of the release date
         */
        @JsonCreator
        public Builder(
                @JsonProperty("date") final Date date,
                @JsonProperty("country") final CountryType country
        ) {
            this.bDate = date;
            this.bCountry = country;
        }

        /**
         * Build the release date.
         *
         * @return Create the final read-only ReleaseDate instance
         */
        public ReleaseDate build() {
            return new ReleaseDate(this);
        }
    }
}
