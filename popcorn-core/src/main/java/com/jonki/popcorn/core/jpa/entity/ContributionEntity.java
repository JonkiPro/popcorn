package com.jonki.popcorn.core.jpa.entity;

import com.jonki.popcorn.common.dto.DataStatus;
import com.jonki.popcorn.common.dto.MovieField;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Representation of the contribution.
 */
@Getter
@Setter
@EqualsAndHashCode(of = "id")
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
    @JoinColumn(name = "movie_id", nullable = false)
    private MovieEntity movie;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "contributions_ids_to_add",
            joinColumns = {
                    @JoinColumn(name = "contribution_id", nullable = false, updatable = false)
            }
    )
    @Column(name = "element_id_to_add", nullable = false, updatable = false)
    private Set<Long> idsToAdd = new HashSet<>();

    @ElementCollection(fetch=FetchType.LAZY)
    @CollectionTable(
            name = "contributions_ids_to_update",
            joinColumns = {
                    @JoinColumn(name = "contribution_id", nullable = false, updatable = false)
            }
    )
    @MapKeyColumn(name = "new_element_id", nullable = false)
    @Column(name = "old_element_id", nullable = false, updatable = false)
    private Map<Long, Long> idsToUpdate = new HashMap<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "contributions_ids_to_delete",
            joinColumns = {
                    @JoinColumn(name = "contribution_id", nullable = false, updatable = false)
            }
    )
    @Column(name = "element_id_to_delete", nullable = false, updatable = false)
    private Set<Long> idsToDelete = new HashSet<>();

    @Basic(optional = false)
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private DataStatus status;

    @Basic(optional = false)
    @Column(name = "field", nullable = false)
    @Enumerated(EnumType.STRING)
    private MovieField field;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "contributions_sources",
            joinColumns = {
                    @JoinColumn(name = "contribution_id", nullable = false, updatable = false)
            }
    )
    @Column(nullable = false, updatable = false)
    private Set<String> sources = new HashSet<>();

    @Basic
    @Column(name = "user_comment")
    private String userComment;

    @Basic(optional = false)
    @Column(name = "created", updatable = false, nullable = false)
    @CreatedDate
    private Date created;

    @Basic
    @Column(name = "verification_date")
    private Date verificationDate;

    @ManyToOne
    @JoinColumn(name = "verification_user")
    private UserEntity verificationUser;

    @Basic
    @Column(name = "verification_comment")
    private String verificationComment;

    @Version
    @Column(name = "entity_version", nullable = false)
    @Getter(AccessLevel.NONE)
    private Integer entityVersion;

    /**
     * Default constructor.
     */
    public ContributionEntity() {
        super();
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
     * Get the user's comment.
     *
     * @return The user's comment
     */
    public Optional<String> getUserComment() {
        return Optional.ofNullable(this.userComment);
    }

    /**
     * Get the verification date.
     *
     * @return The verification date
     */
    public Optional<Date> getVerificationDate() {
        return Optional.ofNullable(this.verificationDate);
    }

    /**
     * Get the verification user.
     *
     * @return The verification user
     */
    public Optional<UserEntity> getVerificationUser() {
        return Optional.ofNullable(this.verificationUser);
    }

    /**
     * Get the verification comment.
     *
     * @return The verification comment
     */
    public Optional<String> getVerificationComment() {
        return Optional.ofNullable(this.verificationComment);
    }
}
