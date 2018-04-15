package com.core.jpa.entity.movie;

import com.common.dto.movie.type.CountryType;
import com.common.dto.MovieField;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

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

    private static final long serialVersionUID = -4457912816896552310L;

    @Basic
    @Column(name = "box_office")
    private BigDecimal boxOffice;

    @Basic
    @Column(name = "box_office_country")
    @Enumerated(EnumType.STRING)
    private CountryType country;
}
