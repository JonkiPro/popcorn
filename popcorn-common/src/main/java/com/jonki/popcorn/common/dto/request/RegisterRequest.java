package com.jonki.popcorn.common.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jonki.popcorn.common.dto.request.validator.PasswordsEqualConstraint;
import com.jonki.popcorn.common.dto.request.validator.ValidEmail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * Fields representing all the values users can set when creating a new User resource. Registration of a new user.
 */
@Getter
@PasswordsEqualConstraint
@JsonDeserialize(builder = RegisterRequest.Builder.class)
@ApiModel(description = "User registration data")
public class RegisterRequest extends CommonRequest {

    private static final long serialVersionUID = -5516403917601928238L;

    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9_-]{6,36}")
    @ApiModelProperty(notes = "The user's name", required = true)
    private String username;

    @NotBlank
    @ValidEmail
    @ApiModelProperty(notes = "The user's e-mail", required = true)
    private String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9])(?=\\S+$).{6,36}$")
    @ApiModelProperty(notes = "The user's password", required = true)
    private String password;

    @NotBlank
    @ApiModelProperty(notes = "The user's password again", required = true)
    private String passwordAgain;

    @ApiModelProperty(notes = "ReCaptcha response", required = true)
    private String reCaptcha;

    /**
     * Constructor only accessible via builder build() method.
     *
     * @param builder The builder to get data from
     */
    private RegisterRequest(final Builder builder) {
        this.username = builder.bUsername;
        this.email = builder.bEmail;
        this.password = builder.bPassword;
        this.passwordAgain = builder.bPasswordAgain;
        this.reCaptcha = builder.bReCaptcha;
    }

    /**
     * A builder to create RegisterRequest.
     */
    public static class Builder {

        private final String bUsername;
        private final String bEmail;
        private final String bPassword;
        private final String bPasswordAgain;
        private final String bReCaptcha;

        /**
         * Constructor which has required fields.
         *
         * @param username The user's name
         * @param email The user's e-mail
         * @param password The user's password
         * @param passwordAgain The user's password again
         * @param reCaptcha ReCaptcha response
         */
        @JsonCreator
        public Builder(
                @JsonProperty("username") final String username,
                @JsonProperty("email") final String email,
                @JsonProperty("password") final String password,
                @JsonProperty("passwordAgain") final String passwordAgain,
                @JsonProperty("reCaptcha") @Nullable final String reCaptcha
        ) {
            this.bUsername = username;
            this.bEmail = email;
            this.bPassword = password;
            this.bPasswordAgain = passwordAgain;
            this.bReCaptcha = reCaptcha;
        }

        /**
         * Build the RegisterRequest.
         *
         * @return Create the final read-only RegisterRequest instance
         */
        public RegisterRequest build() {
            return new RegisterRequest(this);
        }
    }
}