package com.jonki.popcorn.common.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jonki.popcorn.common.dto.movie.type.MovieType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.annotation.Nullable;

/**
 * Read only data transfer object representing a UserMovie.
 */
@Getter
@JsonDeserialize(builder = UserMovie.Builder.class)
@ApiModel(description = "Movie details with additional fields for the logged in user")
public class UserMovie extends Movie {

    private static final long serialVersionUID = -6500104361727649701L;

    @ApiModelProperty(notes = "Rating of the movie of the logged-in user")
    private final Float yourRating;

    @ApiModelProperty(notes = "True if the movie is in the user's favorite movies")
    private final boolean favorited;

    /**
     * Constructor only accessible via builder build() method.
     *
     * @param builder The builder to get data from
     */
    private UserMovie(final Builder builder) {
        super(builder);
        this.yourRating = builder.bYourRating;
        this.favorited = builder.bFavorited;
    }

    /**
     * A builder to create user movies.
     */
    public static class Builder extends Movie.Builder<Builder> {

        private Float bYourRating;
        private boolean bFavorited;

        /**
         * Constructor which has required fields.
         *
         * @param title The original title of the movie
         * @param type  The type of the movie
         */
        @JsonCreator
        public Builder(
                @JsonProperty("title")
                final String title,
                @JsonProperty("type")
                final MovieType type
        ) {
            super(title, type);
        }

        /**
         * Constructor initializing the builder with data from the movie.
         *
         * @param movie The DTO object of the movie with the data to be initialized
         */
        public Builder(
                final Movie movie
        ) {
            super(movie.getTitle(), movie.getType());
            this.withId(movie.getId());
            this.withRating(movie.getRating());
            this.withNumberOfRating(movie.getNumberOfRatings());
            this.withTitleLocated(movie.getTitleLocated());
            this.withReleaseDate(movie.getReleaseDate());
            this.withCountries(movie.getCountries());
            this.withLanguages(movie.getLanguages());
            this.withGenres(movie.getGenres());
            this.withBoxofficeCumulative(movie.getBoxofficeCumulative());
            this.withOutline(movie.getOutline());
            this.withSummary(movie.getSummary());
        }

        /**
         * Set the your rating for movie.
         *
         * @param yourRating Your movie rating
         * @return The builder
         */
        public Builder withYourRating(@Nullable final Float yourRating) {
            this.bYourRating = yourRating;
            return this;
        }

        /**
         * Set whether the movie is added to favorites.
         *
         * @param favorited True if added to favourites
         * @return The builder
         */
        public Builder withFavorited(final boolean favorited) {
            this.bFavorited = favorited;
            return this;
        }

        /**
         * Build the user movie.
         *
         * @return Create the final read-only UserMovie instance
         */
        public UserMovie build() {
            return new UserMovie(this);
        }
    }
}
