package com.jonki.popcorn.core.jpa.entity;

import com.jonki.popcorn.common.dto.DataStatus;
import com.jonki.popcorn.common.dto.movie.type.MovieType;
import com.jonki.popcorn.core.jpa.entity.movie.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Representation of the movie.
 */
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "movies")
public class MovieEntity implements Serializable {

    private static final long serialVersionUID = -2444745076534077956L;

    @Id
    @Basic(optional = false)
    @Column(unique = true, nullable = false, updatable = false)
    @GenericGenerator(
            name = "movieSequenceGenerator", strategy = "enhanced-sequence",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "optimizer", value = "pooled-lo"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "5")
            }
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movieSequenceGenerator")
    private Long id;

    @Basic(optional = false)
    @Column(name = "title", nullable = false)
    private String title;

    @Basic
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private MovieType type;

    @OneToMany(mappedBy = "movie", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("id ASC")
    private List<MovieOtherTitleEntity> otherTitles = new ArrayList<>();

    @OneToMany(mappedBy = "movie", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("date ASC")
    private List<MovieReleaseDateEntity> releaseDates = new ArrayList<>();

    @OneToMany(mappedBy = "movie", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MovieOutlineEntity> outlines = new ArrayList<>();

    @OneToMany(mappedBy = "movie", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MovieSummaryEntity> summaries = new ArrayList<>();

    @OneToMany(mappedBy = "movie", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MovieSynopsisEntity> synopses = new ArrayList<>();

    @OneToMany(mappedBy = "movie", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MovieBoxOfficeEntity> boxOffices = new ArrayList<>();

    @OneToMany(mappedBy = "movie", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MovieSiteEntity> sites = new ArrayList<>();

    @OneToMany(mappedBy = "movie", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MovieCountryEntity> countries = new ArrayList<>();

    @OneToMany(mappedBy = "movie", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MovieLanguageEntity> languages = new ArrayList<>();

    @OneToMany(mappedBy = "movie", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MovieGenreEntity> genres = new ArrayList<>();

    @OneToMany(mappedBy = "movie", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MovieReviewEntity> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "movie", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MoviePhotoEntity> photos = new ArrayList<>();

    @OneToMany(mappedBy = "movie", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MoviePosterEntity> posters = new ArrayList<>();

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MovieRateEntity> ratings = new ArrayList<>();

    @Basic
    @Column(name = "budget")
    private String budget;

    @Basic(optional = false)
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private DataStatus status;

    @Basic
    @Column(name = "rating")
    private Float rating;

    @Basic
    @Column(name = "favorite_count", nullable = false)
    private Integer favoriteCount;

    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY)
    @OrderBy("created ASC")
    private List<ContributionEntity> contributions = new ArrayList<>();

    /**
     * Assign the status "WAITING" when saving a object and the status is null.
     * Initializing the favorite counter to zero.
     */
    @PrePersist
    protected void onCreateMovieEntity() {
        if(this.status == null) {
            status = DataStatus.WAITING;
        }
        if (this.favoriteCount == null) {
            this.favoriteCount = 0;
        }
    }

    /**
     * Get the movie's rating.
     *
     * @return The movie's rating
     */
    public Optional<Float> getRating() {
        return Optional.ofNullable(this.rating);
    }
}
