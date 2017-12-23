package com.common.dto;

import com.common.dto.movie.type.MovieType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
@JsonDeserialize(builder = Movie.Builder.class)
@ApiModel(description = "Movie details")
public class Movie extends BaseDTO {

    private static final long serialVersionUID = -7868660912767218319L;

    @ApiModelProperty(notes = "The title of the movie", required = true)
    private final String title;

    @ApiModelProperty(notes = "The type of the movie", required = true)
    private final MovieType type;

    /**
     * Constructor only accessible via builder build() method.
     *
     * @param builder The builder to get data from
     */
    private Movie(final Builder builder) {
        super(builder);
        this.title = builder.bTitle;
        this.type = builder.bType;
    }

    /**
     * A builder to create movies.
     */
    public static class Builder extends BaseDTO.Builder<Builder> {

        private final String bTitle;
        private final MovieType bType;

        /**
         * Constructor which has required fields.
         *
         * @param title The title of the movie
         * @param type The type of the movie
         */
        @JsonCreator
        public Builder(
                @JsonProperty("title")
                final String title,
                @JsonProperty("type")
                final MovieType type
        ) {
            this.bTitle = title;
            this.bType = type;
        }

        /**
         * Build the movie.
         *
         * @return Create the final read-only Movie instance
         */
        public Movie build() {
            return new Movie(this);
        }
    }
}
