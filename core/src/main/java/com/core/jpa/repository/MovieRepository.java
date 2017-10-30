package com.core.jpa.repository;

import com.core.movie.EditStatus;
import com.core.jpa.entity.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * Movie repository.
 */
public interface MovieRepository extends JpaRepository<MovieEntity, Long>, JpaSpecificationExecutor {

    Optional<MovieEntity> findById(Long id);

    Optional<MovieEntity> findByIdAndStatus(Long id, EditStatus editStatus);
}
