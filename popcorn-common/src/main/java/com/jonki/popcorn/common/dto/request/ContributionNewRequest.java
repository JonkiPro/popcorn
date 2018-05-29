package com.jonki.popcorn.common.dto.request;

import com.jonki.popcorn.common.dto.movie.MovieInfoDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Fields representing all the values users can set when creating a new Contribution resource.
 * @param <T> A class type representing the DTO with information about the movie.
 */
@Getter
@NoArgsConstructor
@ApiModel(description = "An object representing a new contribution")
public class ContributionNewRequest<T extends MovieInfoDTO> extends CommonRequest {

    private static final long serialVersionUID = -5646714920444796543L;

    @ApiModelProperty(notes = "Elements to be added <A new element>")
    @Valid
    private List<T> elementsToAdd = new ArrayList<>();

    @ApiModelProperty(notes = "Elements to be updated <The ID of the updated element, A new element>")
    @Valid
    private Map<Long, T> elementsToUpdate = new HashMap<>();

    @ApiModelProperty(notes = "Element IDs to be deleted <The ID of the element to be deleted>")
    private Set<Long> idsToDelete = new HashSet<>();

    @ApiModelProperty(notes = "Sources of information(elements)", required = true)
    @NotEmpty
    private Set<String> sources = new HashSet<>();

    @ApiModelProperty(notes = "Comment from the user")
    private String comment;

    /**
     * Constructor only accessible via builder build() method.
     *
     * @param builder The builder to get data from
     */
    @SuppressWarnings("unchecked")
    private ContributionNewRequest(final Builder builder) {
        this.elementsToAdd = builder.bElementsToAdd;
        this.elementsToUpdate = builder.bElementsToUpdate;
        this.idsToDelete = builder.bIdsToDelete;
        this.sources = builder.bSources;
        this.comment = builder.bComment;
    }

    /**
     * A builder to create ContributionNewRequest.
     */
    public static class Builder<T> {

        private final List<T> bElementsToAdd = new ArrayList<>();
        private final Map<Long, T> bElementsToUpdate = new HashMap<>();
        private final Set<Long> bIdsToDelete = new HashSet<>();
        private final Set<String> bSources;
        private String bComment;

        /**
         * Constructor which has required fields.
         *
         * @param sources Sources of information(elements)
         */
        public Builder(
                final Set<String> sources
        ) {
            this.bSources = sources;
        }

        /**
         * Set elements to add on this contribution.
         *
         * @param elementsToAdd Elements to add
         * @return The builder
         */
        public Builder withElementsToAdd(@Nullable final List<T> elementsToAdd) {
            this.bElementsToAdd.clear();
            if(elementsToAdd != null) {
                this.bElementsToAdd.addAll(elementsToAdd);
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
         * Set ids to delete on this contribution.
         *
         * @param idsToDelete Ids to delete
         * @return The builder
         */
        public Builder withIdsToDelete(@Nullable final Set<Long> idsToDelete) {
            this.bIdsToDelete.clear();
            if(idsToDelete != null) {
                this.bIdsToDelete.addAll(idsToDelete);
            }
            return this;
        }

        /**
         * Set the user's comment on this contribution.
         *
         * @param comment The user's comment
         * @return The builder
         */
        public Builder withComment(@Nullable final String comment) {
            this.bComment = comment;
            return this;
        }

        /**
         * Build the ContributionNewRequest.
         *
         * @return Create the final read-only ContributionNewRequest instance
         */
        public ContributionNewRequest build() {
            return new ContributionNewRequest(this);
        }
    }
}
