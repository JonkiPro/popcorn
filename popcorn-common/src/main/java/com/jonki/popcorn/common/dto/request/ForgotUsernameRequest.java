package com.jonki.popcorn.common.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jonki.popcorn.common.dto.request.validator.ValidEmail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

/**
 * Fields representing all the values users can set when creating a ForgotPasswordRequest DTO to recover the username.
 */
@Getter
@JsonDeserialize(builder = ForgotUsernameRequest.Builder.class)
@ApiModel(description = "Data to recover the username")
public class ForgotUsernameRequest extends CommonRequest {

    private static final long serialVersionUID = 6640634320194977663L;

    @NotBlank
    @ValidEmail
    @ApiModelProperty(notes = "The user's e-mail", required = true)
    private String email;

    /**
     * Constructor only accessible via builder build() method.
     *
     * @param builder The builder to get data from
     */
    private ForgotUsernameRequest(final Builder builder) {
        this.email = builder.bEmail;
    }

    /**
     * A builder to create ForgotUsernameRequest.
     */
    public static class Builder {

        private final String bEmail;

        /**
         * Constructor which has required fields.
         *
         * @param email The user's e-mail
         */
        @JsonCreator
        public Builder(
                @JsonProperty("email") final String email
        ) {
            this.bEmail = email;
        }

        /**
         * Build the ForgotUsernameRequest.
         *
         * @return Create the final read-only ForgotUsernameRequest instance
         */
        public ForgotUsernameRequest build() {
            return new ForgotUsernameRequest(this);
        }
    }
}
