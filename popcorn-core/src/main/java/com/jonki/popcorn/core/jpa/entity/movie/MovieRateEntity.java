package com.jonki.popcorn.core.jpa.entity.movie;

import com.jonki.popcorn.core.jpa.entity.IdEntity;
import com.jonki.popcorn.core.jpa.entity.MovieEntity;
import com.jonki.popcorn.core.jpa.entity.UserEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    @NotNull
    private MovieEntity movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private UserEntity user;

    @Basic(optional = false)
    @Column(name = "rate", nullable = false)
    @NotNull
    @Min(value = 1)
    @Max(value = 10)
    private Integer rate;

    @Basic(optional = false)
    @Column(nullable = false)
    @CreatedDate
    @LastModifiedDate
    @Setter(AccessLevel.NONE)
    private Date date;
}
