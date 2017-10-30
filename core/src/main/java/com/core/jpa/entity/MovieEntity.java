package com.core.jpa.entity;

import com.common.dto.Movie;
import com.core.movie.EditStatus;
import com.common.dto.movie.MovieType;
import com.core.jpa.entity.movie.*;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Representation of the movie.
 */
@Entity
@Table(name = "movies")
@Data
public class MovieEntity implements Serializable {

    private static final long serialVersionUID = -2444745076534077956L;

    @Id
    @Column(unique = true, updatable = false)
    @GeneratedValue
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private MovieType type;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private Set<MovieDescription> descriptions;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private Set<MovieOtherTitle> otherTitles;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private Set<MovieReview> reviews;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private Set<MovieStoryline> storylines;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private Set<MovieBoxOffice> boxOffices;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private Set<MovieReleaseDate> releaseDates;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private Set<MovieSite> sites;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private Set<MovieCountry> countries;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private Set<MovieLanguage> languages;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private Set<MovieGenre> genres;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private Set<MovieRating> ratings;

    private String budget;

    @Enumerated(EnumType.STRING)
    private EditStatus status;

    /**
     * Constructor - init all sets.
     */
    public MovieEntity() {
        this.descriptions = new HashSet<>();
        this.otherTitles = new HashSet<>();
        this.reviews = new HashSet<>();
        this.storylines = new HashSet<>();
        this.boxOffices = new HashSet<>();
        this.releaseDates = new HashSet<>();
        this.sites = new HashSet<>();
        this.countries = new HashSet<>();
        this.languages = new HashSet<>();
        this.genres = new HashSet<>();
        this.ratings = new HashSet<>();
    }

    /**
     * Assign the status "WAITING" when saving a object and the status is null.
     */
    @PrePersist
    protected void onCreateMovieEntity() {
        if(this.status == null) {
            status = EditStatus.WAITING;
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
}
