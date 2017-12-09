package com.core.jpa.repository;

import com.core.jpa.entity.movie.MovieInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Movie Info repository.
 */
public interface MovieInfoRepository extends JpaRepository<MovieInfoEntity, Long> {

}
