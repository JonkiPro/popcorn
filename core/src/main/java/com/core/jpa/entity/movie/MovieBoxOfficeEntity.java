package com.core.jpa.entity.movie;

import com.common.dto.movie.type.CountryType;
import com.common.dto.MovieField;
import lombok.*;

import javax.persistence.*;

/**
 * Representation of the movie box office.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
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
}
