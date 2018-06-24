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
 * Fields representing all the values users can set when creating a ChangeEmailRequest DTO to change the e-mail.
 */
@Getter
@JsonDeserialize(builder = ChangeEmailRequest.Builder.class)
@ApiModel(description = "Data to change user address email")
public class ChangeEmailRequest extends CommonRequest {

    private static final long serialVersionUID = 4997364125867153959L;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9])(?=\\S+$).{6,36}$")
    @ApiModelProperty(notes = "The user's password", required = true)
    private String password;

    @NotBlank
    @ValidEmail
    @ApiModelProperty(notes = "The user's new e-mail", required = true)
    private String email;

    /**
     * Constructor only accessible via builder build() method.
     *
     * @param builder The builder to get data from
     */
    private ChangeEmailRequest(final Builder builder) {
        this.password = builder.bPassword;
        this.email = builder.bEmail;
    }

    /**
     * A builder to create ChangeEmailRequest.
     */
    public static class Builder {

        private final String bPassword;
        private final String bEmail;

        /**
         * Constructor which has required fields.
         *
         * @param password The user's password
         * @param email The user's e-mail
         */
        @JsonCreator
        public Builder(
                @JsonProperty("password") final String password,
                @JsonProperty("email") final String email
        ) {
            this.bPassword = password;
            this.bEmail = email;
        }

        /**
         * Build the ChangeEmailRequest.
         *
         * @return Create the final read-only ChangeEmailRequest instance
         */
        public ChangeEmailRequest build() {
            return new ChangeEmailRequest(this);
        }
    }
}
