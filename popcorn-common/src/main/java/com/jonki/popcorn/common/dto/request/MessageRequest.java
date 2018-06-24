package com.jonki.popcorn.common.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Fields representing all the values users can set when creating a new Message resource.
 */
@Getter
@JsonDeserialize(builder = MessageRequest.Builder.class)
@ApiModel(description = "New message data")
public class MessageRequest extends CommonRequest {

    private static final long serialVersionUID = 1315035334566054222L;

    @NotBlank
    @ApiModelProperty(notes = "The recipient's username", required = true)
    private String to;

    @NotBlank
    @Size(min = 1, max = 255)
    @ApiModelProperty(notes = "The message subject", required = true)
    private String subject;

    @NotBlank
    @Size(min = 1, max = 4000)
    @ApiModelProperty(notes = "The message text", required = true)
    private String text;

    /**
     * Constructor only accessible via builder build() method.
     *
     * @param builder The builder to get data from
     */
    private MessageRequest(final Builder builder) {
        this.to = builder.bTo;
        this.subject = builder.bSubject;
        this.text = builder.bText;
    }

    /**
     * A builder to create MessageRequest.
     */
    public static class Builder {

        private final String bTo;
        private final String bSubject;
        private final String bText;

        /**
         * Constructor which has required fields.
         *
         * @param to The recipient's username
         * @param subject The message subject
         * @param text The message text
         */
        @JsonCreator
        public Builder(
                @JsonProperty("to") final String to,
                @JsonProperty("subject") final String subject,
                @JsonProperty("text") final String text
        ) {
            this.bTo = to;
            this.bSubject = subject;
            this.bText = text;
        }

        /**
         * Build the MessageRequest.
         *
         * @return Create the final read-only MessageRequest instance
         */
        public MessageRequest build() {
            return new MessageRequest(this);
        }
    }
}
