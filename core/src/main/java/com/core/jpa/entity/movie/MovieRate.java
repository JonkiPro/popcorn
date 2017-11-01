package com.core.jpa.entity.movie;

import com.core.jpa.entity.MovieEntity;
import com.core.jpa.entity.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Representation of the movie rate.
 */
@Entity
@Table(name = "movies_ratings")
@Getter
@Setter
public class MovieRate {

    @Id
    @Column(unique = true, updatable = false)
    @GeneratedValue
    private Long id;

    @ManyToOne
    private MovieEntity movie;

    @ManyToOne
    private UserEntity user;

    private Integer rate;
}
