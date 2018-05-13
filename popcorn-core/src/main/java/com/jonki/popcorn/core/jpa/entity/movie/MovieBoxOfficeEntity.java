package com.jonki.popcorn.core.jpa.entity.movie;

import com.jonki.popcorn.common.dto.MovieField;
import com.jonki.popcorn.common.dto.movie.type.CountryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
