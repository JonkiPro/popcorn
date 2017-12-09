package com.core.jpa.entity.movie;

import com.common.dto.movie.type.CountryType;
import com.common.dto.movie.BoxOffice;
import com.core.movie.MovieField;
import lombok.*;

import javax.persistence.*;

/**
 * Representation of the movie box office.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@DiscriminatorValue(value = MovieField.Values.BOX_OFFICE)
public class MovieBoxOfficeEntity extends MovieInfoEntity {

    private static final long serialVersionUID = -5218345980795995085L;

    @Basic
    @Column(name = "box_office")
    private String boxOffice;

    @Basic
    @Column(name = "box_office_country")
    @Enumerated(EnumType.STRING)
    private CountryType country;


    /**
     * Get a DTO representing this box office.
     *
     * @return The read-only DTO.
     */
    public BoxOffice getDTO() {
        return BoxOffice.builder()
                .boxOffice(this.boxOffice)
                .country(this.country)
                .build();
    }
}
