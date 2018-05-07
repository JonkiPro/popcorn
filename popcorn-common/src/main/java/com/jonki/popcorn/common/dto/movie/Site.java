package com.jonki.popcorn.common.dto.movie;

import com.jonki.popcorn.common.dto.movie.type.SiteType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@JsonDeserialize(builder = Site.Builder.class)
@ApiModel(description = "The website of the movie")
public class Site extends MovieInfoDTO {

    private static final long serialVersionUID = 786955849540188164L;

    @ApiModelProperty(notes = "The website", required = true)
    @NotEmpty
    @URL
    private final String site;

    @ApiModelProperty(notes = "Type of website", required = true)
    @NotNull
    private final SiteType official;

    /**
     * Constructor only accessible via builder build() method.
     *
     * @param builder The builder to get data from
     */
    private Site(final Builder builder) {
        this.site = builder.bSite;
        this.official = builder.bOfficial;
    }

    /**
     * A builder to create sites.
     */
    public static class Builder {

        private final String bSite;
        private final SiteType bOfficial;

        /**
         * Constructor which has required fields.
         *
         * @param site The site of the movie
         * @param official The type of the site
         */
        @JsonCreator
        public Builder(
                @JsonProperty("site") final String site,
                @JsonProperty("official") final SiteType official
        ) {
            this.bSite = site;
            this.bOfficial = official;
        }

        /**
         * Build the site.
         *
         * @return Create the final read-only Site instance
         */
        public Site build() {
            return new Site(this);
        }
    }
}
