package com.jonki.popcorn.common.dto.movie;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * Outline DTO. The class serves both as a resource and a resource creation request.
 */
@Getter
@JsonDeserialize(builder = Outline.Builder.class)
@ApiModel(description = "The outline of the movie")
public class Outline extends MovieInfoDTO {

    private static final long serialVersionUID = 3120469308354882392L;

    @ApiModelProperty(notes = "The outline", required = true)
    @NotEmpty
    @Size(max = 239)
    private final String outline;

    /**
     * Constructor only accessible via builder build() method.
     *
     * @param builder The builder to get data from
     */
    private Outline(final Outline.Builder builder) {
        this.outline = builder.bOutline;
    }

    /**
     * A builder to create outlines.
     */
    public static class Builder {

        private final String bOutline;

        /**
         * Constructor which has required fields.
         *
         * @param outline The outline of the movie
         */
        @JsonCreator
        public Builder(
                @JsonProperty("outline") final String outline
        ) {
            this.bOutline = outline;
        }

        /**
         * Build the outline.
         *
         * @return Create the final read-only Outline instance
         */
        public Outline build() {
            return new Outline(this);
        }
    }
}
