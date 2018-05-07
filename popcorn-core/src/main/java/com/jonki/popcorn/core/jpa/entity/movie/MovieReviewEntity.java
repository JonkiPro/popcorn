package com.jonki.popcorn.core.jpa.entity.movie;

import com.jonki.popcorn.common.dto.MovieField;
import lombok.*;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.Size;

/**
 * Representation of the movie review.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@DiscriminatorValue(value = MovieField.Values.REVIEW)
public class MovieReviewEntity extends MovieInfoEntity {

    private static final long serialVersionUID = -8359258086063115493L;

    @Basic
    @Column(name = "review_title", length = 200)
    @Size(max = 200)
    private String title;

    @Basic
    @Column(name = "review", length = 20000)
    @Size(max = 20000)
    private String review;

    @Basic
    @Column(name = "review_spoiler")
    private boolean spoiler;
}
