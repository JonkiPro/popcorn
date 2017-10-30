package com.core.jpa.entity;

import com.core.movie.EditStatus;
import com.core.movie.MovieField;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Representation of the contribution.
 */
@Entity
@Table(name = "contributions")
@Data
public class ContributionEntity implements Serializable {

    private static final long serialVersionUID = -5037570588750675602L;

    @Id
    @Column(updatable = false)
    @GeneratedValue
    private Long id;

    @ManyToOne
    private MovieEntity movie;

    @ManyToOne
    private UserEntity user;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name="contributions_ids",
            joinColumns=@JoinColumn(name="contribution_id")
    )
    private Set<Long> ids;

    @Enumerated(EnumType.STRING)
    private EditStatus status;

    @Enumerated(EnumType.STRING)
    private MovieField field;

    /**
     * Assign the status "WAITING" when saving a object and the status is null.
     */
    @PrePersist
    protected void onCreateContributionEntity() {
        if(this.status == null) {
            status = EditStatus.WAITING;
        }
    }
}
