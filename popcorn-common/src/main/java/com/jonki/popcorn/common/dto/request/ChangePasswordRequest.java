package com.jonki.popcorn.common.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * Fields representing all the values users can set when creating a ChangePasswordRequest DTO to change the password.
 */
@Getter
@JsonDeserialize(builder = ChangePasswordRequest.Builder.class)
@ApiModel(description = "Data to change user password")
public class ChangePasswordRequest extends CommonRequest {

    private static final long serialVersionUID = 9059041954958259279L;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9])(?=\\S+$).{6,36}$")
    @ApiModelProperty(notes = "The user's password", required = true)
    private String oldPassword;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9])(?=\\S+$).{6,36}$")
    @ApiModelProperty(notes = "The user's new password", required = true)
    private String newPassword;

    @NotBlank
    @ApiModelProperty(notes = "Again the user's new password", required = true)
    private String newPasswordAgain;

    /**
     * Constructor only accessible via builder build() method.
     *
     * @param builder The builder to get data from
     */
    private ChangePasswordRequest(final Builder builder) {
        this.oldPassword = builder.bOldPassword;
        this.newPassword = builder.bNewPassword;
        this.newPasswordAgain = builder.bNewPasswordAgain;
    }

    /**
     * A builder to create ChangePasswordRequest.
     */
    public static class Builder {

        private final String bOldPassword;
        private final String bNewPassword;
        private final String bNewPasswordAgain;

        /**
         * Constructor which has required fields.
         *
         * @param oldPassword The user's password
         * @param newPassword The new password
         * @param newPasswordAgain The new password again
         */
        @JsonCreator
        public Builder(
                @JsonProperty("oldPassword") final String oldPassword,
                @JsonProperty("newPassword") final String newPassword,
                @JsonProperty("newPasswordAgain") final String newPasswordAgain
        ) {
            this.bOldPassword = oldPassword;
            this.bNewPassword = newPassword;
            this.bNewPasswordAgain = newPasswordAgain;
        }

        /**
         * Build the ChangePasswordRequest.
         *
         * @return Create the final read-only ChangePasswordRequest instance
         */
        public ChangePasswordRequest build() {
            return new ChangePasswordRequest(this);
        }
    }
}
