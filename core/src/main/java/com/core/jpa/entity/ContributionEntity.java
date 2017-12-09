package com.core.jpa.entity;

import com.common.dto.Contribution;
import com.common.dto.search.ContributionSearchResult;
import com.core.movie.DataStatus;
import com.core.movie.MovieField;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * Representation of the contribution.
 */
@Getter
@Setter
@Entity
@Table(name = "contributions")
@EntityListeners(AuditingEntityListener.class)
public class ContributionEntity implements Serializable {

    private static final long serialVersionUID = -5037570588750675602L;

    @Id
    @Basic(optional = false)
    @Column(unique = true, nullable = false, updatable = false)
    @GenericGenerator(
            name = "contributionSequenceGenerator", strategy = "enhanced-sequence",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "optimizer", value = "pooled-lo"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "5")
            }
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contributionSequenceGenerator")
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private MovieEntity movie;

    @ManyToOne
    @JoinColumn(nullable = false)
    private UserEntity user;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "contributions_ids_to_add",
            joinColumns = @JoinColumn(name = "contribution_id", nullable = false, updatable = false)
    )
    @Column(name = "element_id_to_add")
    private List<Long> idsToAdd;

    @ElementCollection(fetch=FetchType.EAGER)
    @CollectionTable(
            name = "contributions_ids_to_update",
            joinColumns = @JoinColumn(name = "contribution_id", nullable = false, updatable = false)
    )
    @MapKeyColumn(name="new_element_id")
    @Column(name = "old_element_id")
    private Map<Long, Long> idsToUpdate;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "contributions_ids_to_delete",
            joinColumns = @JoinColumn(name = "contribution_id", nullable = false, updatable = false)
    )
    @Column(name = "element_id_to_delete")
    private List<Long> idsToDelete;

    @Basic(optional = false)
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DataStatus status;

    @Basic(optional = false)
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MovieField field;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "contributions_sources",
            joinColumns = @JoinColumn(name = "contribution_id", nullable = false, updatable = false)
    )
    private Set<String> sources;

    @Basic
    @Column(name = "user_comment")
    private String userComment;

    @Basic(optional = false)
    @Column(name = "creation_date", updatable = false, nullable = false)
    @CreatedDate
    private Date creationDate;

    @Basic
    @Column(name = "verification_date")
    private Date verificationDate;

    @ManyToOne
    @JoinColumn(name = "verification_user")
    private UserEntity verificationUser;

    @Basic
    @Column(name = "verification_comment")
    private String verificationComment;

    /**
     * Constructor - init all lists, sets, maps.
     */
    public ContributionEntity() {
        this.idsToAdd = new ArrayList<>();
        this.idsToUpdate = new HashMap<>();
        this.idsToDelete = new ArrayList<>();
        this.sources = new HashSet<>();
    }

    /**
     * Assign the status "WAITING" when saving a object and the status is null.
     */
    @PrePersist
    protected void onCreateContributionEntity() {
        if(this.status == null) {
            status = DataStatus.WAITING;
        }
    }


    /**
     * Get a DTO representing this contribution.
     *
     * @return The read-only DTO.
     */
    public Contribution getDTO() {
        return Contribution.builder()
                .id(this.id)
                .elementsToAdd(new HashMap<>())
                .elementsToUpdate(new HashMap<>())
                .elementsToDelete(new HashMap<>())
                .elementsUpdated(new HashMap<>())
                .movieId(this.movie.getId())
                .movieTitle(this.movie.getTitle())
                .username(user != null ? this.user.getUsername() : null)
                .status(this.status.toString())
                .field(this.field.toString())
                .sources(this.sources)
                .userComment(userComment != null ? userComment : "")
                .creationDate(this.creationDate)
                .verificationDate(verificationDate)
                .verificationUsername(this.verificationUser != null ? verificationUser.getUsername() : null)
                .verificationComment(this.verificationComment != null ? verificationComment : "")
                .build();
    }

    /**
     * Get a DTO representing this contribution.
     *
     * @return The read-only DTO.
     */
    public ContributionSearchResult getSearchResultDTO() {
        return ContributionSearchResult.builder()
                .id(this.id)
                .field(this.field.toString())
                .date(this.creationDate)
                .build();
    }
}
