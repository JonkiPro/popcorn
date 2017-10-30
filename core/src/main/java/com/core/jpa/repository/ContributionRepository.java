package com.core.jpa.repository;

import com.core.movie.EditStatus;
import com.core.jpa.entity.ContributionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Contribution repository.
 */
public interface ContributionRepository extends JpaRepository<ContributionEntity, Long> {

    Optional<ContributionEntity> findByIdAndStatus(Long id, EditStatus editStatus);
}
