package com.jonki.popcorn.core.jpa.entity.movie;

import com.jonki.popcorn.common.dto.MovieField;
import com.jonki.popcorn.common.dto.movie.type.SiteType;
import lombok.*;

import javax.persistence.*;

/**
 * Representation of the movie website.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@DiscriminatorValue(value = MovieField.Values.SITE)
public class MovieSiteEntity extends MovieInfoEntity {

    private static final long serialVersionUID = 3557203018075079464L;

    @Basic
    @Column(name = "site")
    private String site;

    @Basic
    @Column(name = "site_official")
    @Enumerated(EnumType.STRING)
    private SiteType official;
}
