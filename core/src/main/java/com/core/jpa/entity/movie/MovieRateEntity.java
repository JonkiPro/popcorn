package com.core.jpa.entity.movie;

import com.common.dto.movie.response.RateResponse;
import com.core.jpa.entity.MovieEntity;
import com.core.jpa.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Representation of the movie rate.
 */
@Getter
@Setter
@Entity
@Table(name = "movies_ratings")
@EntityListeners(AuditingEntityListener.class)
public class MovieRateEntity implements Serializable {

    private static final long serialVersionUID = 2630710767750390210L;

    @Id
    @Basic(optional = false)
    @Column(unique = true, nullable = false, updatable = false)
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private MovieEntity movie;

    @ManyToOne
    @JoinColumn(nullable = false)
    private UserEntity user;

    @Basic(optional = false)
    @Column(nullable = false)
    private Integer rate;

    @Basic(optional = false)
    @Column(nullable = false)
    @CreatedDate
    @LastModifiedDate
    private Date date;


    /**
     * Get a DTO representing this rate.
     *
     * @return The read-only DTO.
     */
    public RateResponse getDTO() {
        return RateResponse.builder()
                .rate(this.rate)
                .user(this.user.getUsername())
                .date(this.date)
                .build();
    }
}
