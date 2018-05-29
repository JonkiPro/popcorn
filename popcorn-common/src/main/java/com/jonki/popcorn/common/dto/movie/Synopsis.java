package com.jonki.popcorn.common.dto.movie;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * Synopsis DTO. The class serves both as a resource and a resource creation request.
 */
@Getter
@JsonDeserialize(builder = Synopsis.Builder.class)
@ApiModel(description = "The synopsis of the movie")
public class Synopsis extends MovieInfoDTO {

    private static final long serialVersionUID = 6359018106715408231L;

    @ApiModelProperty(notes = "The synopsis", required = true)
    @NotEmpty
    @Size(max = 100000)
    private final String synopsis;

    /**
     * Constructor only accessible via builder build() method.
     *
     * @param builder The builder to get data from
     */
    private Synopsis(final Synopsis.Builder builder) {
        this.synopsis = builder.bSynopsis;
    }

    /**
     * A builder to create synopses.
     */
    public static class Builder {

        private final String bSynopsis;

        /**
         * Constructor which has required fields.
         *
         * @param synopsis The synopsis of the movie
         */
        @JsonCreator
        public Builder(
                @JsonProperty("synopsis") final String synopsis
        ) {
            this.bSynopsis = synopsis;
        }

        /**
         * Build the synopsis.
         *
         * @return Create the final read-only Synopsis instance
         */
        public Synopsis build() {
            return new Synopsis(this);
        }
    }
}
