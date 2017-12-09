package com.core.jpa.repository;

import com.core.jpa.entity.UserEntity;
import com.core.movie.DataStatus;
import com.core.jpa.entity.ContributionEntity;
import com.core.movie.MovieField;
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
    Optional<ContributionEntity> findByIdAndField(Long id, MovieField field);

    /**
     * Find contribution by ID and status.
     *
     * @param id The contribution ID
     * @param dataStatus The status
     * @return The contribution
     */
    Optional<ContributionEntity> findByIdAndStatus(Long id, DataStatus dataStatus);

    /**
     * Find contribution by ID and status and user and movie field.
     *
     * @param id The contribution ID
     * @param dataStatus The status
     * @param user The UserEntity object
     * @param field The movie field
     * @return The contribution
     */
    Optional<ContributionEntity> findByIdAndStatusAndUserAndField(Long id, DataStatus dataStatus, UserEntity user, MovieField field);
}
