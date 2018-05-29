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
 * Summary DTO. The class serves both as a resource and a resource creation request.
 */
@Getter
@JsonDeserialize(builder = Summary.Builder.class)
@ApiModel(description = "The summary of the movie")
public class Summary extends MovieInfoDTO {

    private static final long serialVersionUID = 5768110653278119686L;

    @ApiModelProperty(notes = "The summary", required = true)
    @NotEmpty
    @Size(min = 239, max = 2000)
    private final String summary;

    /**
     * Constructor only accessible via builder build() method.
     *
     * @param builder The builder to get data from
     */
    private Summary(final Summary.Builder builder) {
        this.summary = builder.bSummary;
    }

    /**
     * A builder to create summaries.
     */
    public static class Builder {

        private final String bSummary;

        /**
         * Constructor which has required fields.
         *
         * @param summary The summary of the movie
         */
        @JsonCreator
        public Builder(
                @JsonProperty("summary") final String summary
        ) {
            this.bSummary = summary;
        }

        /**
         * Build the summary.
         *
         * @return Create the final read-only Summary instance
         */
        public Summary build() {
            return new Summary(this);
        }
    }
}
