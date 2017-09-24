package com.service.app.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "messages")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Message {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User recipient;

    @Column(nullable = false)
    private String subject;

    @Column(length = 4000, nullable = false)
    private String text;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private Date date;

    @Column(name = "date_of_read")
    private Date dateOfRead;

    @Column(name = "visible_for_sender", nullable = false)
    private boolean isVisibleForSender;

    @Column(name = "visible_for_recipient", nullable = false)
    private boolean isVisibleForRecipient;
}
