package com.jonki.popcorn.core.jpa.entity.movie;

import com.jonki.popcorn.common.dto.MovieField;
import com.jonki.popcorn.common.dto.TitleAttribute;
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

/**
 * Representation of another movie title.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@DiscriminatorValue(value = MovieField.Values.OTHER_TITLE)
public class MovieOtherTitleEntity extends MovieInfoEntity {

    private static final long serialVersionUID = 2522515435913741105L;

    @Basic
    @Column(name = "title")
    private String title;

    @Basic
    @Column(name = "title_country")
    @Enumerated(EnumType.STRING)
    private CountryType country;

    @Basic
    @Column(name = "title_attribute")
    @Enumerated(EnumType.STRING)
    private TitleAttribute attribute;
}
