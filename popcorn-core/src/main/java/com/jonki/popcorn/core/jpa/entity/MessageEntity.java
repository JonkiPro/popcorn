package com.jonki.popcorn.core.jpa.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Optional;

/**
 * Representation of the message.
 */
@Getter
@Setter
@ToString(callSuper = true, of = {"subject", "text", "dateOfRead", "isVisibleForSender", "isVisibleForRecipient"})
@Entity
@Table(name = "messages")
public class MessageEntity extends BaseEntity {

    private static final long serialVersionUID = 6761068804530627981L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    @NotNull
    private UserEntity sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    @NotNull
    private UserEntity recipient;

    @Basic(optional = false)
    @Column(name = "subject", nullable = false)
    @NotBlank
    @Size(min = 1, max = 255)
    private String subject;

    @Basic(optional = false)
    @Column(name = "text", length = 4000, nullable = false)
    @NotBlank
    @Size(min = 1, max = 4000)
    private String text;

    @Basic
    @Column(name = "date_of_read")
    private Date dateOfRead;

    @Basic(optional = false)
    @Column(name = "visible_for_sender", nullable = false)
    private boolean isVisibleForSender;

    @Basic(optional = false)
    @Column(name = "visible_for_recipient", nullable = false)
    private boolean isVisibleForRecipient;

    /**
     * Default constructor.
     */
    public MessageEntity() {
        super();
    }

    /**
     * Get the date of read of this message.
     *
     * @return The date of read
     */
    public Optional<Date> getDateOfRead() {
        return Optional.ofNullable(this.dateOfRead);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object o) {
        return super.equals(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
