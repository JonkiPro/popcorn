package com.core.jpa.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Representation of the friendship.
 */
@Entity
@Table(name = "friendship")
@Data
@NoArgsConstructor
public class FriendshipEntity implements Serializable {

    private static final long serialVersionUID = 5729044871052669360L;

    @Id
    @Column(updatable = false)
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private UserEntity fromUser;

    @ManyToOne
    @JoinColumn(nullable = false)
    private UserEntity toUser;

    public FriendshipEntity(final UserEntity fromUser, final UserEntity toUser) {
        this.fromUser = fromUser;
        this.toUser = toUser;
    }
}
