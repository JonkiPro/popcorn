package com.core.jpa.entity.movie;

import com.core.movie.MovieField;
import com.common.dto.movie.SiteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Representation of the movie website.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@DiscriminatorValue(value = MovieField.Values.SITE)
public class MovieSite extends MovieInfo {

    private String site;

    @Enumerated(EnumType.STRING)
    @Column(name = "site_official")
    private SiteType official;
}
