package com.service.app.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "invitations")
@Data
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Invitation {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User fromUser;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User toUser;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private Date date;

    public Invitation(User fromUser, User toUser) {
        this.fromUser = fromUser;
        this.toUser = toUser;
    }
}
