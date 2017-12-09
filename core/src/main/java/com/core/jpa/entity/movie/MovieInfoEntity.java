package com.core.jpa.entity.movie;

import com.core.movie.DataStatus;
import com.core.movie.MovieField;
import com.core.jpa.entity.MovieEntity;
import com.core.jpa.entity.UserEntity;
import com.core.utils.EnumUtils;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Representation of the movie info. Base class for other classes that store movie data.
 */
@Getter
@Setter
@Entity
@Table(name = "movies_info")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public class MovieInfoEntity implements Serializable {

    private static final long serialVersionUID = -414335505393277302L;

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
    protected UserEntity user;

    @Basic(optional = false)
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DataStatus status;

    @Basic(optional = false)
    @Column(nullable = false)
    private boolean reportedForUpdate;

    @Basic(optional = false)
    @Column(nullable = false)
    private boolean reportedForDelete;

    /**
     * Assign the status "WAITING" when saving a object and the status is null.
     */
    @PrePersist
    protected void onCreateMovieInfoEntity() {
        if(this.status == null) {
            status = DataStatus.WAITING;
        }
    }

    /**
     * Get the field of movie by the value of discrimination.
     *
     * @return Movie field
     */
    @Transient
    public MovieField getDiscriminatorValue(){
        DiscriminatorValue val = this.getClass().getAnnotation( DiscriminatorValue.class );

        return val == null ? null : EnumUtils.getEnumFromString(MovieField.class, val.value());
    }
}
