package com.jonki.popcorn.common.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jonki.popcorn.common.dto.movie.type.MovieType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Fields representing all the values users can set when creating a new Movie resource.
 */
@Getter
@JsonDeserialize(builder = MovieRequest.Builder.class)
@ApiModel(description = "Data on the new movie")
public class MovieRequest extends CommonRequest {

    private static final long serialVersionUID = -1596297267694643241L;

    @NotBlank
    @ApiModelProperty(notes = "Title of the movie", required = true)
    private String title;

    @NotNull
    @ApiModelProperty(notes = "Type of movie", required = true)
    private MovieType type;

    /**
     * Constructor only accessible via builder build() method.
     *
     * @param builder The builder to get data from
     */
    private MovieRequest(final Builder builder) {
        this.title = builder.bTitle;
        this.type = builder.bType;
    }

    /**
     * A builder to create MovieRequest.
     */
    public static class Builder {

        private final String bTitle;
        private final MovieType bType;

        /**
         * Constructor which has required fields.
         *
         * @param title Title of the movie
         * @param type Type of movie
         */
        @JsonCreator
        public Builder(
                @JsonProperty("title") final String title,
                @JsonProperty("type") final MovieType type
        ) {
            this.bTitle = title;
            this.bType = type;
        }

        /**
         * Build the MovieRequest.
         *
         * @return Create the final read-only MovieRequest instance
         */
        public MovieRequest build() {
            return new MovieRequest(this);
        }
    }
}
