package com.jonki.popcorn.core.jpa.repository;

import com.jonki.popcorn.core.jpa.entity.movie.MovieInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Movie Info repository.
 */
public interface MovieInfoRepository extends JpaRepository<MovieInfoEntity, Long> {

}
