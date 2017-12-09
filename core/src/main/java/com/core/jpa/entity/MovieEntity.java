package com.core.jpa.entity;

import com.common.dto.Movie;
import com.common.dto.search.MovieSearchResult;
import com.core.movie.DataStatus;
import com.common.dto.movie.type.MovieType;
import com.core.jpa.entity.movie.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representation of the movie.
 */
@Getter
@Setter
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
    @Column(nullable = false)
    private String title;

    @Basic
    @Enumerated(EnumType.STRING)
    private MovieType type;

    @OneToMany(mappedBy = "movie", orphanRemoval = true, cascade = CascadeType.ALL)
    @OrderBy("id ASC")
    private List<MovieOtherTitleEntity> otherTitles;

    @OneToMany(mappedBy = "movie", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<MovieReleaseDateEntity> releaseDates;

    @OneToMany(mappedBy = "movie", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<MovieStorylineEntity> storylines;

    @OneToMany(mappedBy = "movie", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<MovieBoxOfficeEntity> boxOffices;

    @OneToMany(mappedBy = "movie", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<MovieSiteEntity> sites;

    @OneToMany(mappedBy = "movie", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<MovieCountryEntity> countries;

    @OneToMany(mappedBy = "movie", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<MovieLanguageEntity> languages;

    @OneToMany(mappedBy = "movie", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<MovieGenreEntity> genres;

    @OneToMany(mappedBy = "movie", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<MovieReviewEntity> reviews;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<MovieRateEntity> ratings;

    @Basic
    private String budget;

    @Basic(optional = false)
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DataStatus status;

    @Basic
    private Float rating;

    @OneToMany(mappedBy = "movie")
    @OrderBy("creationDate ASC")
    private List<ContributionEntity> contributions;

    /**
     * Constructor - init all sets.
     */
    public MovieEntity() {
        this.otherTitles = new ArrayList<>();
        this.reviews = new ArrayList<>();
        this.storylines = new ArrayList<>();
        this.boxOffices = new ArrayList<>();
        this.releaseDates = new ArrayList<>();
        this.sites = new ArrayList<>();
        this.countries = new ArrayList<>();
        this.languages = new ArrayList<>();
        this.genres = new ArrayList<>();
        this.ratings = new ArrayList<>();
    }

    /**
     * Assign the status "WAITING" when saving a object and the status is null.
     */
    @PrePersist
    protected void onCreateMovieEntity() {
        if(this.status == null) {
            status = DataStatus.WAITING;
        }
    }



    /**
     * Get a DTO representing this movie.
     *
     * @return The read-only DTO.
     */
    public Movie getDTO() {
        return Movie.builder()
                .id(this.id)
                .title(this.title)
                .type(this.type)
                .build();
    }

    /**
     * Get a DTO representing this movie.
     *
     * @return The read-only DTO.
     */
    public MovieSearchResult getSearchResultDTO() {
        return MovieSearchResult.builder()
                .id(this.id)
                .title(this.title)
                .type(this.type)
                .rating(this.rating)
                .build();
    }
}
