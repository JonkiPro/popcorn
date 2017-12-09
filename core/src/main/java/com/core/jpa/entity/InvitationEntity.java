package com.core.jpa.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Representation of the invitation.
 */
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "invitations")
@EntityListeners(AuditingEntityListener.class)
public class InvitationEntity implements Serializable {

    private static final long serialVersionUID = 563181989395575248L;

    @Id
    @Basic(optional = false)
    @Column(unique = true, nullable = false, updatable = false)
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_user_id", nullable = false)
    private UserEntity fromUser;

    @ManyToOne
    @JoinColumn(name = "to_user_id", nullable = false)
    private UserEntity toUser;

    @Basic(optional = false)
    @Column(updatable = false, nullable = false)
    @CreatedDate
    private Date date;

    public InvitationEntity(final UserEntity fromUser, final UserEntity toUser) {
        this.fromUser = fromUser;
        this.toUser = toUser;
    }
}
