package com.common.dto;

/**
 * The user's right to accept or reject contributions with movie data for specific fields.
 */
public enum UserMoviePermission {
    /**
     * All permissions
     */
    ALL,
    /**
     * Only new movies
     */
    NEW_MOVIE,
    /**
     * The remaining permissions are only for specific fields
     */
    TITLE,
    TYPE,
    OTHER_TITLE,
    DESCRIPTION,
    REVIEW,
    BUDGET,
    BOX_OFFICE,
    SITE,
    RELEASE_DATE,
    STORYLINE,
    COUNTRY,
    GENRE,
    LANGUAGE,
}
