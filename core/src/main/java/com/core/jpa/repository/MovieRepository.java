package com.core.jpa.repository;

import com.core.movie.DataStatus;
import com.core.jpa.entity.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * Movie repository.
 */
public interface MovieRepository extends JpaRepository<MovieEntity, Long>, JpaSpecificationExecutor {

    /**
     * Find movie by ID and status.
     *
     * @param id The movie ID
     * @param dataStatus The status
     * @return The movie
     */
    Optional<MovieEntity> findByIdAndStatus(Long id, DataStatus dataStatus);
}
