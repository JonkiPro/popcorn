package com.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.Date;

@Getter
@ApiModel(description = "Message data")
public abstract class Message extends BaseDTO {

    private static final long serialVersionUID = -3438463060008868311L;

    @ApiModelProperty(notes = "The message subject", required = true)
    private final String subject;

    @ApiModelProperty(notes = "The message text", required = true)
    private final String text;

    @ApiModelProperty(notes = "The message date of sent", required = true)
    private final Date date;

    /**
     * Constructor only accessible via builder build() method.
     *
     * @param builder The builder to get data from
     */
    protected Message(final Builder builder) {
        super(builder);
        this.subject = builder.bSubject;
        this.text = builder.bText;
        this.date = builder.bDate;
    }

    /**
     * A builder to create messages.
     */
    public abstract static class Builder<T extends Builder> extends BaseDTO.Builder<T> {

        private final String bSubject;
        private final String bText;
        private final Date bDate;

        /**
         * Constructor which has required fields.
         *
         * @param subject The message subject
         * @param text The message text
         * @param date The message date of sent
         */
        protected Builder(final String subject, final String text, final Date date) {
            this.bSubject = subject;
            this.bText = text;
            this.bDate = date;
        }
    }
}
