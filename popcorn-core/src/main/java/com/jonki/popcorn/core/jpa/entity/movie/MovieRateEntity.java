package com.jonki.popcorn.core.jpa.entity.movie;

import com.jonki.popcorn.core.jpa.entity.IdEntity;
import com.jonki.popcorn.core.jpa.entity.MovieEntity;
import com.jonki.popcorn.core.jpa.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * Representation of the movie rate.
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@Table(name = "movies_ratings")
@EntityListeners(AuditingEntityListener.class)
public class MovieRateEntity extends IdEntity {

    private static final long serialVersionUID = 2630710767750390210L;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private MovieEntity movie;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Basic(optional = false)
    @Column(name = "rate", nullable = false)
    private Integer rate;

    @Basic(optional = false)
    @Column(nullable = false)
    @CreatedDate
    @LastModifiedDate
    private Date date;
}
