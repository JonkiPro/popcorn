package com.jonki.popcorn.common.dto.movie;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jonki.popcorn.common.dto.movie.type.GenreType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotNull;

/**
 * Genre DTO. The class serves both as a resource and a resource creation request.
 */
@Getter
@JsonDeserialize(builder = Genre.Builder.class)
@ApiModel(description = "The genre of the movie")
public class Genre extends MovieInfoDTO {

    private static final long serialVersionUID = 8586559057529219762L;

    @ApiModelProperty(notes = "The genre", required = true)
    @NotNull
    private final GenreType genre;

    /**
     * Constructor only accessible via builder build() method.
     *
     * @param builder The builder to get data from
     */
    private Genre(final Builder builder) {
        this.genre = builder.bGenre;
    }

    /**
     * A builder to create genres.
     */
    public static class Builder {

        private final GenreType bGenre;

        /**
         * Constructor which has required fields.
         *
         * @param genre The genre of the movie
         */
        @JsonCreator
        public Builder(
                @JsonProperty("genre") final GenreType genre
        ) {
            this.bGenre = genre;
        }

        /**
         * Build the genre.
         *
         * @return Create the final read-only Genre instance
         */
        public Genre build() {
            return new Genre(this);
        }
    }
}
