package com.core.jpa.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * Representation of the invitation.
 */
@Entity
@Table(name = "invitations")
@Data
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class InvitationEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private UserEntity fromUser;

    @ManyToOne
    @JoinColumn(nullable = false)
    private UserEntity toUser;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private Date date;

    public InvitationEntity(final UserEntity fromUser, final UserEntity toUser) {
        this.fromUser = fromUser;
        this.toUser = toUser;
    }
}
