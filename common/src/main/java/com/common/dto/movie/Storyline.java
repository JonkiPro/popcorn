package com.common.dto.movie;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

@Getter
@JsonDeserialize(builder = Storyline.Builder.class)
@ApiModel(description = "The storyline of the movie")
public class Storyline extends MovieInfoDTO {

    private static final long serialVersionUID = 2046769063919725055L;

    @ApiModelProperty(notes = "The storyline", required = true)
    @NotEmpty
    private final String storyline;

    /**
     * Constructor only accessible via builder build() method.
     *
     * @param builder The builder to get data from
     */
    private Storyline(final Builder builder) {
        this.storyline = builder.bStoryline;
    }

    /**
     * A builder to create storylines.
     */
    public static class Builder {

        private final String bStoryline;

        /**
         * Constructor which has required fields.
         *
         * @param storyline The storyline of the movie
         */
        @JsonCreator
        public Builder(
                @JsonProperty("storyline") final String storyline
        ) {
            this.bStoryline = storyline;
        }

        /**
         * Build the storyline.
         *
         * @return Create the final read-only Storyline instance
         */
        public Storyline build() {
            return new Storyline(this);
        }
    }
}
