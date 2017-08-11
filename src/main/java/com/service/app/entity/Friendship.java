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

    @Column(name = "from_id")
    private Long fromId;

    @Column(name = "to_id")
    private Long toId;

    public Friendship(Long fromId, Long toId) {
        this.fromId = fromId;
        this.toId = toId;
    }
}
