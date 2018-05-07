package com.jonki.popcorn.common.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.Date;

@Getter
@JsonDeserialize(builder = MessageSent.Builder.class)
@ApiModel(description = "Message data sent")
public class MessageSent extends Message {

    private static final long serialVersionUID = -1737283506913214334L;

    @ApiModelProperty(notes = "The recipient's username", required = true)
    private final ShallowUser recipient;

    /**
     * Constructor only accessible via builder build() method.
     *
     * @param builder The builder to get data from
     */
    private MessageSent(final Builder builder) {
        super(builder);
        this.recipient = builder.bRecipient;
    }

    /**
     * A builder to create sent messages.
     */
    public static class Builder extends Message.Builder<Builder> {

        private final ShallowUser bRecipient;

        /**
         * Constructor which has required fields.
         *
         * @param subject The message subject
         * @param text The message text
         * @param date The message date of sent
         * @param recipient The recipient's name
         */
        @JsonCreator
        public Builder(
                @JsonProperty("subject")
                final String subject,
                @JsonProperty("text")
                final String text,
                @JsonProperty("date")
                final Date date,
                @JsonProperty("recipient")
                final ShallowUser recipient
        ) {
            super(subject, text, date);
            this.bRecipient = recipient;
        }

        /**
         * Build the sent messages.
         *
         * @return Create the final read-only MessageSent instance
         */
        public MessageSent build() {
            return new MessageSent(this);
        }
    }
}
