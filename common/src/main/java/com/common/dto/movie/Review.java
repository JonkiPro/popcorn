package com.common.dto.movie;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

@Getter
@JsonDeserialize(builder = Review.Builder.class)
@ApiModel(description = "The review of the movie")
public class Review extends MovieInfoDTO {

    private static final long serialVersionUID = 9058991364875721355L;

    @ApiModelProperty(notes = "The title", required = true)
    @NotEmpty
    @Size(max = 64)
    private final String title;

    @ApiModelProperty(notes = "The review", required = true)
    @NotEmpty
    private final String review;

    /**
     * Constructor only accessible via builder build() method.
     *
     * @param builder The builder to get data from
     */
    private Review(final Builder builder) {
        this.title = builder.bTitle;
        this.review = builder.bReview;
    }

    /**
     * A builder to create reviews.
     */
    public static class Builder {

        private final String bTitle;
        private final String bReview;

        /**
         * Constructor which has required fields.
         *
         * @param title The title of review of the movie
         * @param review The review of the movie
         */
        @JsonCreator
        public Builder(
                @JsonProperty("title") final String title,
                @JsonProperty("review") final String review
        ) {
            this.bTitle = title;
            this.bReview = review;
        }

        /**
         * Build the review.
         *
         * @return Create the final read-only Review instance
         */
        public Review build() {
            return new Review(this);
        }
    }
}
