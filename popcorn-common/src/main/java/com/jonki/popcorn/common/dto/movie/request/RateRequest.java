package com.jonki.popcorn.common.dto.movie.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jonki.popcorn.common.dto.request.CommonRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Fields representing all the values users can set when creating a new Rate resource.
 */
@Getter
@JsonDeserialize(builder = RateRequest.Builder.class)
@ApiModel(description = "Movie rating")
public class RateRequest extends CommonRequest {

    private static final long serialVersionUID = 6181912334372906796L;

    @ApiModelProperty(notes = "Movie rating", required = true)
    @NotNull
    @Min(1)
    @Max(10)
    private Integer rate;

    /**
     * Constructor only accessible via builder build() method.
     *
     * @param builder The builder to get data from
     */
    private RateRequest(final Builder builder) {
        this.rate = builder.bRate;
    }

    /**
     * A builder to create RateRequest.
     */
    public static class Builder {

        private final Integer bRate;

        /**
         * Constructor which has required fields.
         *
         * @param rate Your rating for the movie
         */
        @JsonCreator
        public Builder(
                @JsonProperty("rate") final Integer rate
        ) {
            this.bRate = rate;
        }

        /**
         * Build the RateRequest.
         *
         * @return Create the final read-only RateRequest instance
         */
        public RateRequest build() {
            return new RateRequest(this);
        }
    }
}
