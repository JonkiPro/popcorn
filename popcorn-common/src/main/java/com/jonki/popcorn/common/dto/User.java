package com.jonki.popcorn.common.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.annotation.Nullable;

@Getter
@JsonDeserialize(builder = User.Builder.class)
@ApiModel(description = "User data")
public class User extends BaseDTO {

    private static final long serialVersionUID = -4279148919241460704L;

    @ApiModelProperty(notes = "The user's name", required = true)
    private final String username;

    @ApiModelProperty(notes = "The user's e-mail", required = true)
    private final String email;

    @ApiModelProperty(notes = "The user's avatar")
    private final String avatarSrc;

    @ApiModelProperty(notes = "The number of friends", required = true)
    private final Integer numberOfFriends;

    /**
     * Constructor only accessible via builder build() method.
     *
     * @param builder The builder to get data from
     */
    private User(final Builder builder) {
        super(builder);
        this.username = builder.bUsername;
        this.email = builder.bEmail;
        this.avatarSrc = builder.bAvatarSrc;
        this.numberOfFriends = builder.bNumberOfFriends;
    }

    /**
     * A builder to create users.
     */
    public static class Builder extends BaseDTO.Builder<Builder> {

        private final String bUsername;
        private final String bEmail;
        private String bAvatarSrc;
        private final Integer bNumberOfFriends;

        /**
         * Constructor which has required fields.
         *
         * @param username The user's name
         * @param email The user's e-mail
         * @param numberOfFriends The number of friends
         */
        @JsonCreator
        public Builder(
                @JsonProperty("username")
                final String username,
                @JsonProperty("email")
                final String email,
                @JsonProperty("numberOfFriends")
                final Integer numberOfFriends
        ) {
            this.bUsername = username;
            this.bEmail = email;
            this.bNumberOfFriends = numberOfFriends;
        }

        /**
         * Set the user's avatar.
         *
         * @param avatarSrc Address (URL) of avatar
         * @return The builder
         */
        public Builder withAvatarSrc(@Nullable final String avatarSrc) {
            this.bAvatarSrc = avatarSrc;
            return this;
        }

        /**
         * Build the user.
         *
         * @return Create the final read-only User instance
         */
        public User build() {
            return new User(this);
        }
    }
}
