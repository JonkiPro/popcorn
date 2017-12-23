package com.common.dto;

import com.common.dto.movie.MovieInfoDTO;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
@JsonDeserialize(builder = Contribution.Builder.class)
@ApiModel(description = "An object representing the contribution")
public class Contribution<T extends MovieInfoDTO> extends BaseDTO {

    private static final long serialVersionUID = -7075491432618511176L;

    @ApiModelProperty(notes = "Elements to be added <The ID of the element, The element>")
    private final Map<Long, T> elementsToAdd;

    @ApiModelProperty(notes = "Elements to be updated <The ID of the element, The element>")
    private final Map<Long, T> elementsToUpdate;

    @ApiModelProperty(notes = "Elements to be deleted <The ID of the element, The element>")
    private final Map<Long, T> elementsToDelete;

    @ApiModelProperty(notes = "Elements updated <The ID of the element, The element>")
    private final Map<Long, T> elementsUpdated;

    @ApiModelProperty(notes = "The movie ID", required = true)
    private final Long movieId;

    @ApiModelProperty(notes = "The title of the movie", required = true)
    private final String movieTitle;

    @ApiModelProperty(notes = "The name of the contribution author", required = true)
    private final String username;

    @ApiModelProperty(notes = "The status of contribution", required = true)
    private final DataStatus status;

    @ApiModelProperty(notes = "The movie field", required = true)
    private final MovieField field;

    @ApiModelProperty(notes = "Sources of information(elements)", required = true)
    private final Set<String> sources;

    @ApiModelProperty(notes = "Comment from the user(author)")
    private final String userComment;

    @ApiModelProperty(notes = "Date of creation of the contribution", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private final Date creationDate;

    @ApiModelProperty(notes = "Date of verification of the contribution")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private final Date verificationDate;

    @ApiModelProperty(notes = "The name of the verification user")
    private final String verificationUsername;

    @ApiModelProperty(notes = "The comment of the verification user")
    private final String verificationComment;

    /**
     * Constructor only accessible via builder build() method.
     *
     * @param builder The builder to get data from
     */
    @SuppressWarnings("unchecked")
    private Contribution(final Builder builder) {
        super(builder);
        this.elementsToAdd = (Map<Long, T>) builder.bElementsToAdd;
        this.elementsToUpdate = (Map<Long, T>) builder.bElementsToUpdate;
        this.elementsToDelete = (Map<Long, T>) builder.bElementsToDelete;
        this.elementsUpdated = (Map<Long, T>) builder.bElementsUpdated;
        this.movieId = builder.bMovieId;
        this.movieTitle = builder.bMovieTitle;
        this.username = builder.bUsername;
        this.status = builder.bStatus;
        this.field = builder.bField;
        this.sources = builder.bSources;
        this.userComment = builder.bUserComment;
        this.creationDate = builder.bCreationDate;
        this.verificationDate = builder.bVerificationDate;
        this.verificationUsername = builder.bVerificationUsername;
        this.verificationComment = builder.bVerificationComment;
    }

    /**
     * A builder to create contributions.
     */
    public static class Builder extends BaseDTO.Builder<Builder> {

        private final Map<Long, ? extends MovieInfoDTO> bElementsToAdd = new HashMap<>();
        private final Map<Long, ? extends MovieInfoDTO> bElementsToUpdate = new HashMap<>();
        private final Map<Long, ? extends MovieInfoDTO> bElementsToDelete = new HashMap<>();
        private final Map<Long, ? extends MovieInfoDTO> bElementsUpdated = new HashMap<>();
        private final Long bMovieId;
        private final String bMovieTitle;
        private final String bUsername;
        private final DataStatus bStatus;
        private final MovieField bField;
        private final Set<String> bSources;
        private String bUserComment;
        private final Date bCreationDate;
        private Date bVerificationDate;
        private String bVerificationUsername;
        private String bVerificationComment;

        /**
         * Constructor which has required fields.
         *
         * @param movieId The movie ID
         * @param movieTitle The title of the movie
         * @param username The author's name
         * @param status The data status
         * @param field The movie field
         * @param sources Sources of information
         * @param creationDate Date of creation of the contribution
         */
        @JsonCreator
        public Builder(
                @JsonProperty("movieId")
                final Long movieId,
                @JsonProperty("movieTitle")
                final String movieTitle,
                @JsonProperty("username")
                final String username,
                @JsonProperty("status")
                final DataStatus status,
                @JsonProperty("field")
                final MovieField field,
                @JsonProperty("sources")
                final Set<String> sources,
                @JsonProperty("creationDate")
                final Date creationDate
        ) {
            this.bMovieId = movieId;
            this.bMovieTitle = movieTitle;
            this.bUsername = username;
            this.bStatus = status;
            this.bField = field;
            this.bSources = sources;
            this.bCreationDate = creationDate;
        }

        /**
         * Set the user's comment on this contribution.
         *
         * @param userComment The user's comment
         * @return The builder
         */
        public Builder withUserComment(@Nullable final String userComment) {
            this.bUserComment = userComment;
            return this;
        }

        /**
         * Set the verification date on this contribution.
         *
         * @param verificationDate The verification date
         * @return The builder
         */
        public Builder withVerificationDate(@Nullable final Date verificationDate) {
            this.bVerificationDate = verificationDate;
            return this;
        }

        /**
         * Set the verifier user name on this contribution.
         *
         * @param verificationUsername The name of the verifying user
         * @return The builder
         */
        public Builder withVerificationUsername(@Nullable final String verificationUsername) {
            this.bVerificationUsername = verificationUsername;
            return this;
        }

        /**
         * Set the verifier comment on this contribution.
         *
         * @param verificationComment Comment of the verification user
         * @return The builder
         */
        public Builder withVerificationComment(@Nullable final String verificationComment) {
            this.bVerificationComment = verificationComment;
            return this;
        }

        /**
         * Build the contribution.
         *
         * @return Create the final read-only Contribution instance
         */
        public Contribution build() {
            return new Contribution(this);
        }
    }
}
