package com.jonki.popcorn.common.dto;

import com.jonki.popcorn.common.dto.movie.ReleaseDate;
import com.jonki.popcorn.common.dto.movie.type.CountryType;
import com.jonki.popcorn.common.dto.movie.type.GenreType;
import com.jonki.popcorn.common.dto.movie.type.LanguageType;
import com.jonki.popcorn.common.dto.movie.type.MovieType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@JsonDeserialize(builder = Movie.Builder.class)
@ApiModel(description = "Movie details")
public class Movie extends BaseDTO {

    private static final long serialVersionUID = -7868660912767218319L;

    @ApiModelProperty(notes = "The original title of the movie", required = true)
    private final String title;

    @ApiModelProperty(notes = "Local title of the movie")
    private final String titleLocated;

    @ApiModelProperty(notes = "The type of the movie", required = true)
    private final MovieType type;

    @ApiModelProperty(notes = "The rating of the movie")
    private final Float rating;

    @ApiModelProperty(notes = "Number of ratings")
    private final Integer numberOfRatings;

    @ApiModelProperty(notes = "Release date")
    private final ReleaseDate releaseDate;

    @ApiModelProperty(notes = "List of countries")
    private final List<CountryType> countries;

    @ApiModelProperty(notes = "List of languages")
    private final List<LanguageType> languages;

    @ApiModelProperty(notes = "List of genres")
    private final List<GenreType> genres;

    @ApiModelProperty(notes = "Total box office")
    private final BigDecimal boxofficeCumulative;

    @ApiModelProperty(notes = "The outline of the movie")
    private final String outline;

    @ApiModelProperty(notes = "The summary of the movie")
    private final String summary;

    /**
     * Constructor only accessible via builder build() method.
     *
     * @param builder The builder to get data from
     */
    @SuppressWarnings("unchecked")
    protected Movie(final Builder builder) {
        super(builder);
        this.title = builder.bTitle;
        this.titleLocated = builder.bTitleLocated;
        this.type = builder.bType;
        this.rating = builder.bRating;
        this.numberOfRatings = builder.bNumberOfRatings;
        this.releaseDate = builder.bReleaseDate;
        this.countries = builder.bCountries;
        this.languages = builder.bLanguages;
        this.genres = builder.bGenres;
        this.boxofficeCumulative = builder.bBoxofficeCumulative;
        this.outline = builder.bOutline;
        this.summary = builder.bSummary;
    }

    /**
     * A builder to create movies.
     */
    public static class Builder<T extends Builder> extends BaseDTO.Builder<T> {

        private final String bTitle;
        private String bTitleLocated;
        private final MovieType bType;
        private Float bRating;
        private Integer bNumberOfRatings;
        private ReleaseDate bReleaseDate;
        private List<CountryType> bCountries;
        private List<LanguageType> bLanguages;
        private List<GenreType> bGenres;
        private BigDecimal bBoxofficeCumulative;
        private String bOutline;
        private String bSummary;

        /**
         * Constructor which has required fields.
         *
         * @param title The original title of the movie
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
         * Set the local movie title.
         *
         * @param titleLocated Local title of the movie
         * @return The builder
         */
        public Builder withTitleLocated(@Nullable final String titleLocated) {
            this.bTitleLocated = titleLocated;
            return this;
        }

        /**
         * Set the movie's rating.
         *
         * @param rating The rating of the movie
         * @return The builder
         */
        public Builder withRating(@Nullable final Float rating) {
            this.bRating = rating;
            return this;
        }

        /**
         * Set the movie's number of ratings.
         *
         * @param numberOfRating Number of ratings
         * @return The builder
         */
        public Builder withNumberOfRating(@Nullable final Integer numberOfRating) {
            this.bNumberOfRatings = numberOfRating;
            return this;
        }

        /**
         * Set the movie's release date.
         *
         * @param releaseDate Release date
         * @return The builder
         */
        public Builder withReleaseDate(@Nullable final ReleaseDate releaseDate) {
            this.bReleaseDate = releaseDate;
            return this;
        }

        /**
         * Set the movie's countries.
         *
         * @param countries List of countries
         * @return The builder
         */
        public Builder withCountries(@Nullable final List<CountryType> countries) {
            this.bCountries = countries;
            return this;
        }

        /**
         * Set the movie's languages.
         *
         * @param languages List of languages
         * @return The builder
         */
        public Builder withLanguages(@Nullable final List<LanguageType> languages) {
            this.bLanguages = languages;
            return this;
        }

        /**
         * Set the movie's genres.
         *
         * @param genres List of genres
         * @return The builder
         */
        public Builder withGenres(@Nullable final List<GenreType> genres) {
            this.bGenres = genres;
            return this;
        }

        /**
         * Set the movie's box office.
         *
         * @param boxofficeCumulative Total box office
         * @return The builder
         */
        public Builder withBoxofficeCumulative(@Nullable final BigDecimal boxofficeCumulative) {
            this.bBoxofficeCumulative = boxofficeCumulative;
            return this;
        }

        /**
         * Set the movie's outline.
         *
         * @param outline The outline of the movie
         * @return The builder
         */
        public Builder withOutline(@Nullable final String outline) {
            this.bOutline = outline;
            return this;
        }

        /**
         * Set the movie's summary.
         *
         * @param summary The summary of the movie
         * @return The builder
         */
        public Builder withSummary(@Nullable final String summary) {
            this.bSummary = summary;
            return this;
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
