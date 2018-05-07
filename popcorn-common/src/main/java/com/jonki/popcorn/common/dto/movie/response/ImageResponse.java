package com.jonki.popcorn.common.dto.movie.response;

import com.jonki.popcorn.common.dto.movie.MovieInfoDTO;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
@JsonDeserialize(builder = ImageResponse.Builder.class)
@ApiModel(description = "The image(photo, poster) of the movie")
public class ImageResponse extends MovieInfoDTO {

    private static final long serialVersionUID = -2888506993270867653L;

    @ApiModelProperty(notes = "Address to the image(photo, poster) file in the cloud", required = true)
    private final String src;

    /**
     * Constructor only accessible via builder build() method.
     *
     * @param builder The builder to get data from
     */
    private ImageResponse(final Builder builder) {
        this.src = builder.bSrc;
    }

    /**
     * A builder to create images.
     */
    public static class Builder {

        private final String bSrc;

        /**
         * Constructor which has required fields.
         *
         * @param src The image src
         */
        @JsonCreator
        public Builder(
                @JsonProperty("src") final String src
        ) {
            this.bSrc = src;
        }

        /**
         * Build the image.
         *
         * @return Create the final read-only Image instance
         */
        public ImageResponse build() {
            return new ImageResponse(this);
        }
    }
}
