package com.core.jpa.entity.movie;

import com.core.movie.EditStatus;
import com.core.movie.MovieField;
import com.core.jpa.entity.MovieEntity;
import com.core.jpa.entity.UserEntity;
import com.core.utils.EnumUtils;
import lombok.Data;

import javax.persistence.*;

/**
 * Representation of the movie info. Base class for other classes that store movie data.
 */
@Entity
@Table(name = "movies_info")
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public class MovieInfo {

    @Id
    @Column(unique = true, updatable = false)
    @GeneratedValue
    private Long id;

    @ManyToOne
    private MovieEntity movie;

    @ManyToOne
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    private EditStatus status;

    /**
     * Assign the status "WAITING" when saving a object and the status is null.
     */
    @PrePersist
    protected void onCreateMovieInfoEntity() {
        if(this.status == null) {
            status = EditStatus.WAITING;
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
