package com.service.app.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "friendship")
@Data
@NoArgsConstructor
public class Friendship {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User fromUser;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User toUser;

    public Friendship(User fromUser, User toUser) {
        this.fromUser = fromUser;
        this.toUser = toUser;
    }
}
