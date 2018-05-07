package com.jonki.popcorn.common.dto.movie.request;

import com.jonki.popcorn.common.dto.movie.MovieInfoDTO;
import com.jonki.popcorn.common.dto.request.validator.ValidImage;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.io.File;

@Getter
@JsonDeserialize(builder = ImageRequest.Builder.class)
@ApiModel(description = "The image(photo, poster) of the movie")
public class ImageRequest extends MovieInfoDTO {

    private static final long serialVersionUID = -2888506993270867653L;

    @ApiModelProperty(notes = "An image file", required = true)
    @ValidImage
    private final File file;

    @ApiModelProperty(notes = "The ID of the image file in the cloud", required = true)
    private final String idInCloud;

    /**
     * Constructor only accessible via builder build() method.
     *
     * @param builder The builder to get data from
     */
    private ImageRequest(final Builder builder) {
        this.file = builder.bFile;
        this.idInCloud = builder.bIdInCloud;
    }

    /**
     * A builder to create images.
     */
    public static class Builder {

        private File bFile;
        private String bIdInCloud;

        @JsonCreator
        public Builder() {
        }

        /**
         * Set the image file.
         *
         * @param file The image file of the movie
         * @return The builder
         */
        public Builder withFile(final File file) {
            this.bFile = file;
            return this;
        }

        /**
         * Set the file identifier in the cloud.
         *
         * @param idInCloud The ID in the cloud
         * @return The builder
         */
        public Builder withIdInCloud(final String idInCloud) {
            this.bIdInCloud = idInCloud;
            return this;
        }

        /**
         * Build the image.
         *
         * @return Create the final read-only Image instance
         */
        public ImageRequest build() {
            return new ImageRequest(this);
        }
    }
}
