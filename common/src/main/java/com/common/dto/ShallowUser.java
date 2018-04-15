package com.common.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
@JsonDeserialize(builder = ShallowUser.Builder.class)
@ApiModel(description = "This type represents a user, but omits many of the fields found on the full User type")
public class ShallowUser extends BaseDTO {

    private static final long serialVersionUID = 4171104156983899542L;

    @ApiModelProperty(notes = "The user's name", required = true)
    private String username;

    @ApiModelProperty(notes = "The avatar src", required = true)
    private String avatarSrc;

    /**
     * Constructor only accessible via builder build() method.
     *
     * @param builder The builder to get data from
     */
    private ShallowUser(final Builder builder) {
        super(builder);
        this.username = builder.bUsername;
        this.avatarSrc = builder.bAvatarSrc;
    }

    /**
     * A builder to create shallow users.
     */
    public static class Builder extends BaseDTO.Builder<ShallowUser.Builder> {

        private final String bUsername;
        private final String bAvatarSrc;

        /**
         * Constructor which has required fields.
         *
         * @param username The user's name
         * @param avatarSrc The avatar src
         */
        @JsonCreator
        public Builder(
                @JsonProperty("username")
                final String username,
                @JsonProperty("avatarSrc")
                final String avatarSrc
        ) {
            this.bUsername = username;
            this.bAvatarSrc = avatarSrc;
        }

        /**
         * Build the shallow user.
         *
         * @return Create the final read-only ShallowUser instance
         */
        public ShallowUser build() {
            return new ShallowUser(this);
        }
    }
}
