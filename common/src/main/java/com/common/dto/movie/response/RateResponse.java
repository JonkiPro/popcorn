package com.common.dto.movie.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.Date;

@Getter
@JsonDeserialize(builder = RateResponse.Builder.class)
@ApiModel(description = "Movie rating")
public class RateResponse {

    @ApiModelProperty(notes = "Movie rating", required = true)
    private final Integer rate;

    @ApiModelProperty(notes = "The user's name", required = true)
    private final String user;

    @ApiModelProperty(notes = "Date of rating", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private final Date date;

    /**
     * Constructor only accessible via builder build() method.
     *
     * @param builder The builder to get data from
     */
    private RateResponse(final Builder builder) {
        this.rate = builder.bRate;
        this.user = builder.bUser;
        this.date = builder.bDate;
    }

    /**
     * A builder to create rates.
     */
    public static class Builder {

        private final Integer bRate;
        private final String bUser;
        private final Date bDate;

        /**
         * Constructor which has required fields.
         *
         * @param rate The rate of the user
         * @param user The user's name
         * @param date The creation date
         */
        public Builder(
                @JsonProperty("rate") final Integer rate,
                @JsonProperty("user") final String user,
                @JsonProperty("date") final Date date
        ) {
            this.bRate = rate;
            this.bUser = user;
            this.bDate = date;
        }

        /**
         * Build the rate.
         *
         * @return Create the final read-only RateResponse instance
         */
        public RateResponse build() {
            return new RateResponse(this);
        }
    }
}
