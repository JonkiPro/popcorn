package com.jonki.popcorn.core.jpa.entity;

import com.jonki.popcorn.common.dto.DataStatus;
import com.jonki.popcorn.common.dto.MovieField;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.URL;
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
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
@EqualsAndHashCode(of = {"id"})
@ToString(
        of = {
                "id",
                "status",
                "field",
                "userComment",
                "created",
                "verificationDate",
                "verificationComment"
        },
        doNotUseGetters = true
)
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
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    @NotNull
    private MovieEntity movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private UserEntity user;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "contributions_ids_to_add",
            joinColumns = {
                    @JoinColumn(name = "contribution_id", nullable = false, updatable = false)
            }
    )
    @Column(name = "element_id_to_add", nullable = false, updatable = false)
    private Set<@Min(1) Long> idsToAdd = new HashSet<>();

    @ElementCollection(fetch=FetchType.LAZY)
    @CollectionTable(
            name = "contributions_ids_to_update",
            joinColumns = {
                    @JoinColumn(name = "contribution_id", nullable = false, updatable = false)
            }
    )
    @MapKeyColumn(name = "new_element_id", nullable = false)
    @Column(name = "old_element_id", nullable = false, updatable = false)
    private Map<@NotNull @Min(1) Long, @NotNull @Min(1) Long> idsToUpdate = new HashMap<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "contributions_ids_to_delete",
            joinColumns = {
                    @JoinColumn(name = "contribution_id", nullable = false, updatable = false)
            }
    )
    @Column(name = "element_id_to_delete", nullable = false, updatable = false)
    private Set<@Min(1) Long> idsToDelete = new HashSet<>();

    @Basic(optional = false)
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private DataStatus status;

    @Basic(optional = false)
    @Column(name = "field", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private MovieField field;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "contributions_sources",
            joinColumns = {
                    @JoinColumn(name = "contribution_id", nullable = false, updatable = false)
            }
    )
    @Column(nullable = false, updatable = false)
    @NotEmpty
    private Set<@URL String> sources = new HashSet<>();

    @Basic
    @Column(name = "user_comment")
    private String userComment;

    @Basic(optional = false)
    @Column(name = "created", updatable = false, nullable = false)
    @CreatedDate
    @Setter(AccessLevel.NONE)
    private Date created;

    @Basic
    @Column(name = "verification_date")
    private Date verificationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verification_user")
    private UserEntity verificationUser;

    @Basic
    @Column(name = "verification_comment")
    private String verificationComment;

    @Version
    @Column(name = "entity_version", nullable = false)
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
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
            this.status = DataStatus.WAITING;
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
