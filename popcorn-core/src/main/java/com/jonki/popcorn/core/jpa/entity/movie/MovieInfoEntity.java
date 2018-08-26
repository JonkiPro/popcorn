package com.jonki.popcorn.core.jpa.entity.movie;

import com.jonki.popcorn.common.dto.DataStatus;
import com.jonki.popcorn.common.dto.MovieField;
import com.jonki.popcorn.core.jpa.entity.MovieEntity;
import com.jonki.popcorn.core.util.EnumUtils;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Representation of the movie info. Base class for other classes that store movie data.
 */
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"id", "status", "reportedForUpdate", "reportedForDelete"})
@Entity
@Table(name = "movies_info")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public class MovieInfoEntity implements Serializable {

    private static final long serialVersionUID = -414335505393277302L;

    @Id
    @Basic(optional = false)
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    @GenericGenerator(
            name = "movieInfoSequenceGenerator", strategy = "enhanced-sequence",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "optimizer", value = "pooled-lo"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "5")
            }
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movieInfoSequenceGenerator")
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    @NotNull
    private MovieEntity movie;

    @Basic(optional = false)
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
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
    @Setter(AccessLevel.NONE)
    private Integer entityVersion;

    /**
     * Assign the status "WAITING" when saving a object and the status is null.
     */
    @PrePersist
    protected void onCreateMovieInfoEntity() {
        if(this.status == null) {
            this.status = DataStatus.WAITING;
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
