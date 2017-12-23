package com.core.jpa.entity.movie;

import com.common.dto.DataStatus;
import com.common.dto.MovieField;
import com.core.jpa.entity.MovieEntity;
import com.core.utils.EnumUtils;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Representation of the movie info. Base class for other classes that store movie data.
 */
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "movies_info")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public class MovieInfoEntity implements Serializable {

    private static final long serialVersionUID = -414335505393277302L;

    @Id
    @Basic(optional = false)
    @Column(unique = true, nullable = false, updatable = false)
    @GenericGenerator(
            name = "movieInfoSequenceGenerator", strategy = "enhanced-sequence",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "optimizer", value = "pooled-lo"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "5")
            }
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movieInfoSequenceGenerator")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private MovieEntity movie;

    @Basic(optional = false)
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private DataStatus status;

    @Basic(optional = false)
    @Column(name = "reported_for_update", nullable = false)
    private boolean reportedForUpdate;

    @Basic(optional = false)
    @Column(name = "reported_for_delete", nullable = false)
    private boolean reportedForDelete;

    @Version
    @Column(name = "entity_version", nullable = false)
    @Getter(AccessLevel.NONE)
    private Integer entityVersion;

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
    @Deprecated
    public MovieField getDiscriminatorValue(){
        final DiscriminatorValue val = this.getClass().getAnnotation( DiscriminatorValue.class );

        return val == null ? null : EnumUtils.getEnumFromString(MovieField.class, val.value());
    }
}
