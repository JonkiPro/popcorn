package com.jonki.popcorn.core.jpa.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * Representation of the message.
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@Table(name = "messages")
public class MessageEntity extends BaseEntity {

    private static final long serialVersionUID = 6761068804530627981L;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private UserEntity sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private UserEntity recipient;

    @Basic(optional = false)
    @Column(name = "subject", nullable = false)
    private String subject;

    @Basic(optional = false)
    @Column(name = "text", length = 4000, nullable = false)
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
}
