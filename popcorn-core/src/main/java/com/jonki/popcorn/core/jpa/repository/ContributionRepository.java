package com.jonki.popcorn.core.jpa.repository;

import com.jonki.popcorn.common.dto.DataStatus;
import com.jonki.popcorn.common.dto.MovieField;
import com.jonki.popcorn.core.jpa.entity.ContributionEntity;
import com.jonki.popcorn.core.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * Contribution repository.
 */
public interface ContributionRepository extends JpaRepository<ContributionEntity, Long>, JpaSpecificationExecutor {

    /**
     * Find contribution by ID and movie field.
     *
     * @param id The contribution ID
     * @param field The movie field
     * @return The contribution
     */
    Optional<ContributionEntity> findByIdAndField(final Long id, final MovieField field);

    /**
     * Find contribution by ID and status.
     *
     * @param id The contribution ID
     * @param dataStatus The status
     * @return The contribution
     */
    Optional<ContributionEntity> findByIdAndStatus(final Long id, final DataStatus dataStatus);

    /**
     * Find contribution by ID and status and user and movie field.
     *
     * @param id The contribution ID
     * @param dataStatus The status
     * @param user The UserEntity object
     * @param field The movie field
     * @return The contribution
     */
    Optional<ContributionEntity> findByIdAndStatusAndUserAndField(final Long id, final DataStatus dataStatus, final UserEntity user, final MovieField field);
}
