package com.core.jpa.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Representation of the friendship.
 */
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "friendship")
public class FriendshipEntity implements Serializable {

    private static final long serialVersionUID = 5729044871052669360L;

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

    public FriendshipEntity(final UserEntity fromUser, final UserEntity toUser) {
        this.fromUser = fromUser;
        this.toUser = toUser;
    }
}
