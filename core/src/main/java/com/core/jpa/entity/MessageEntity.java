package com.core.jpa.entity;

import com.common.dto.MessageReceived;
import com.common.dto.MessageSent;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Representation of the message.
 */
@Getter
@Setter
@Entity
@Table(name = "messages")
@EntityListeners(AuditingEntityListener.class)
public class MessageEntity implements Serializable {

    private static final long serialVersionUID = 6761068804530627981L;

    @Id
    @Basic(optional = false)
    @Column(unique = true, nullable = false, updatable = false)
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private UserEntity sender;

    @ManyToOne
    @JoinColumn(nullable = false)
    private UserEntity recipient;

    @Basic(optional = false)
    @Column(nullable = false)
    private String subject;

    @Basic(optional = false)
    @Column(length = 4000, nullable = false)
    private String text;

    @Basic(optional = false)
    @Column(updatable = false, nullable = false)
    @CreatedDate
    private Date date;

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
     * Get a DTO representing this received message.
     *
     * @return The read-only DTO.
     */
    public MessageReceived getReceivedDTO() {
        return MessageReceived.builder()
                .id(this.id)
                .sender(this.sender.getUsername())
                .subject(this.subject)
                .text(this.text)
                .date(this.date)
                .dateOfRead(this.dateOfRead)
                .build();
    }

    /**
     * Get a DTO representing this sent message.
     *
     * @return The read-only DTO.
     */
    public MessageSent getSentDTO() {
        return MessageSent.builder()
                .id(this.id)
                .recipient(this.recipient.getUsername())
                .subject(this.subject)
                .text(this.text)
                .date(this.date)
                .build();
    }
}
