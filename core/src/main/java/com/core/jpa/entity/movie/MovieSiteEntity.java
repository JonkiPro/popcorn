package com.core.jpa.entity.movie;

import com.common.dto.movie.Site;
import com.core.movie.MovieField;
import com.common.dto.movie.type.SiteType;
import lombok.*;

import javax.persistence.*;

/**
 * Representation of the movie website.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@DiscriminatorValue(value = MovieField.Values.SITE)
public class MovieSiteEntity extends MovieInfoEntity {

    private static final long serialVersionUID = 3557203018075079464L;

    @Basic
    private String site;

    @Basic
    @Column(name = "site_official")
    @Enumerated(EnumType.STRING)
    private SiteType official;


    /**
     * Get a DTO representing this site.
     *
     * @return The read-only DTO.
     */
    public Site getDTO() {
        return Site.builder()
                .site(this.site)
                .official(this.official)
                .build();
    }
}
