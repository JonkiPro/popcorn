package com.jonki.popcorn.common.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jonki.popcorn.common.dto.request.validator.ValidEmail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * Fields representing all the values users can set when creating a ForgotPasswordRequest DTO to reset the password.
 */
@Getter
@JsonDeserialize(builder = ForgotPasswordRequest.Builder.class)
@ApiModel(description = "Data to reset a user password")
public class ForgotPasswordRequest extends CommonRequest {

    private static final long serialVersionUID = 3728429163858236109L;

    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9_-]{6,36}")
    @ApiModelProperty(notes = "The user's name", required = true)
    private String username;

    @NotBlank
    @ValidEmail
    @ApiModelProperty(notes = "The user's e-mail", required = true)
    private String email;

    /**
     * Constructor only accessible via builder build() method.
     *
     * @param builder The builder to get data from
     */
    private ForgotPasswordRequest(final Builder builder) {
        this.username = builder.bUsername;
        this.email = builder.bEmail;
    }

    /**
     * A builder to create ForgotPasswordRequest.
     */
    public static class Builder {

        private final String bUsername;
        private final String bEmail;

        /**
         * Constructor which has required fields.
         *
         * @param username The user's name
         * @param email The user's e-mail
         */
        @JsonCreator
        public Builder(
                @JsonProperty("username") final String username,
                @JsonProperty("email") final String email
        ) {
            this.bUsername = username;
            this.bEmail = email;
        }

        /**
         * Build the ForgotPasswordRequest.
         *
         * @return Create the final read-only ForgotPasswordRequest instance
         */
        public ForgotPasswordRequest build() {
            return new ForgotPasswordRequest(this);
        }
    }
}
