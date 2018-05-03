package com.common.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.Date;

@Getter
@JsonDeserialize(builder = MessageReceived.Builder.class)
@ApiModel(description = "Message data received")
public class MessageReceived extends Message {

    private static final long serialVersionUID = -4764452135987289104L;

    @ApiModelProperty(notes = "The sender's username", required = true)
    private final ShallowUser sender;

    @ApiModelProperty(notes = "The message date of read")
    private final Date dateOfRead;

    /**
     * Constructor only accessible via builder build() method.
     *
     * @param builder The builder to get data from
     */
    private MessageReceived(final Builder builder) {
        super(builder);
        this.sender = builder.bSender;
        this.dateOfRead = builder.bDateOfRead;
    }

    /**
     * A builder to create received messages.
     */
    public static class Builder extends Message.Builder<Builder> {

        private final ShallowUser bSender;
        private final Date bDateOfRead;

        /**
         * Constructor which has required fields.
         *
         * @param subject The message subject
         * @param text The message text
         * @param date The message date of sent
         * @param sender The sender user
         * @param dateOfRead The movie ID
         */
        @JsonCreator
        public Builder(
                @JsonProperty("subject")
                final String subject,
                @JsonProperty("text")
                final String text,
                @JsonProperty("date")
                final Date date,
                @JsonProperty("sender")
                final ShallowUser sender,
                @JsonProperty("dateOfRead")
                final Date dateOfRead
        ) {
            super(subject, text, date);
            this.bSender = sender;
            this.bDateOfRead = dateOfRead;
        }

        /**
         * Build the received messages.
         *
         * @return Create the final read-only MessageReceived instance
         */
        public MessageReceived build() {
            return new MessageReceived(this);
        }
    }
}
