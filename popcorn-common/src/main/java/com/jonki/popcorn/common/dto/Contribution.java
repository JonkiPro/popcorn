package com.jonki.popcorn.common.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jonki.popcorn.common.dto.movie.MovieInfoDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Read only data transfer object representing a Contribution.
 * @param <T> A class type representing the DTO with information about the movie.
 */
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
    private final ShallowUser owner;

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

    @ApiModelProperty(notes = "The verification user")
    private final ShallowUser verificationUser;

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
        this.owner = builder.bOwner;
        this.status = builder.bStatus;
        this.field = builder.bField;
        this.sources = builder.bSources;
        this.userComment = builder.bUserComment;
        this.creationDate = builder.bCreationDate;
        this.verificationDate = builder.bVerificationDate;
        this.verificationUser = builder.bVerificationUser;
        this.verificationComment = builder.bVerificationComment;
    }

    /**
     * A builder to create contributions.
     */
    public static class Builder<T> extends BaseDTO.Builder<Builder> {

        private final Map<Long, T> bElementsToAdd = new HashMap<>();
        private final Map<Long, T> bElementsToUpdate = new HashMap<>();
        private final Map<Long, T> bElementsToDelete = new HashMap<>();
        private final Map<Long, T> bElementsUpdated = new HashMap<>();
        private final Long bMovieId;
        private final String bMovieTitle;
        private final ShallowUser bOwner;
        private final DataStatus bStatus;
        private final MovieField bField;
        private final Set<String> bSources;
        private String bUserComment;
        private final Date bCreationDate;
        private Date bVerificationDate;
        private ShallowUser bVerificationUser;
        private String bVerificationComment;

        /**
         * Constructor which has required fields.
         *
         * @param movieId The movie ID
         * @param movieTitle The title of the movie
         * @param owner The author's name
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
                @JsonProperty("owner")
                final ShallowUser owner,
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
            this.bOwner = owner;
            this.bStatus = status;
            this.bField = field;
            this.bSources = sources;
            this.bCreationDate = creationDate;
        }

        /**
         * Set elements to add on this contribution.
         *
         * @param elementsToAdd Elements to add
         * @return The builder
         */
        public Builder withElementsToAdd(@Nullable final Map<Long, T> elementsToAdd) {
            this.bElementsToAdd.clear();
            if(elementsToAdd != null) {
                this.bElementsToAdd.putAll(elementsToAdd);
            }
            return this;
        }

        /**
         * Set elements to update on this contribution.
         *
         * @param elementsToUpdate Elements to update
         * @return The builder
         */
        public Builder withElementsToUpdate(@Nullable final Map<Long, T> elementsToUpdate) {
            this.bElementsToUpdate.clear();
            if(elementsToUpdate != null) {
                this.bElementsToUpdate.putAll(elementsToUpdate);
            }
            return this;
        }

        /**
         * Set elements to delete on this contribution.
         *
         * @param elementsToDelete Elements to delete
         * @return The builder
         */
        public Builder withElementsToDelete(@Nullable final Map<Long, T> elementsToDelete) {
            this.bElementsToDelete.clear();
            if(elementsToDelete != null) {
                this.bElementsToDelete.putAll(elementsToDelete);
            }
            return this;
        }

        /**
         * Set elements updated on this contribution.
         *
         * @param elementsUpdated Elements updated
         * @return The builder
         */
        public Builder withElementsUpdated(@Nullable final Map<Long, T> elementsUpdated) {
            this.bElementsUpdated.clear();
            if(elementsUpdated != null) {
                this.bElementsUpdated.putAll(elementsUpdated);
            }
            return this;
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
         * Set the verifier user on this contribution.
         *
         * @param verificationUser The verifying user
         * @return The builder
         */
        public Builder withVerificationUser(@Nullable final ShallowUser verificationUser) {
            this.bVerificationUser = verificationUser;
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
