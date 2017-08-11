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

    private Long sender;

    private Long recipient;

    private String subject;

    private String text;

    @CreatedDate
    @Column(updatable = false)
    private Date date;

    @Column(name = "date_of_read")
    private Date dateOfRead;

    @Column(name = "visible_for_sender")
    private boolean isVisibleForSender;

    @Column(name = "visible_for_recipient")
    private boolean isVisibleForRecipient;
}
