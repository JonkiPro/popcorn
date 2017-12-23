package com.core.jpa.entity.movie;

import com.common.dto.MovieField;
import lombok.*;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

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

    private static final long serialVersionUID = 8523470845354612514L;

    @Basic
    @Column(name = "review_title")
    private String title;

    @Basic
    @Column(name = "review")
    private String review;
}
