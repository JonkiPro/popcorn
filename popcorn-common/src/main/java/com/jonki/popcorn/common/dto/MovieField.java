package com.jonki.popcorn.common.dto;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Movie fields.
 */
public enum MovieField {
    TITLE(Values.TITLE) {
        @Override
        public Set<UserMoviePermission> getNecessaryPermissions() {
            return Sets.newHashSet(UserMoviePermission.ALL, UserMoviePermission.TITLE);
        }
    },
    TYPE(Values.TYPE) {
        @Override
        public Set<UserMoviePermission> getNecessaryPermissions() {
            return Sets.newHashSet(UserMoviePermission.ALL, UserMoviePermission.TYPE);
        }
    },
    OTHER_TITLE(Values.OTHER_TITLE) {
        @Override
        public Set<UserMoviePermission> getNecessaryPermissions() {
            return Sets.newHashSet(UserMoviePermission.ALL, UserMoviePermission.OTHER_TITLE);
        }
    },
    REVIEW(Values.REVIEW) {
        @Override
        public Set<UserMoviePermission> getNecessaryPermissions() {
            return Sets.newHashSet(UserMoviePermission.ALL, UserMoviePermission.REVIEW);
        }
    },
    BUDGET(Values.BUDGET) {
        @Override
        public Set<UserMoviePermission> getNecessaryPermissions() {
            return Sets.newHashSet(UserMoviePermission.ALL, UserMoviePermission.BUDGET);
        }
    },
    BOX_OFFICE(Values.BOX_OFFICE) {
        @Override
        public Set<UserMoviePermission> getNecessaryPermissions() {
            return Sets.newHashSet(UserMoviePermission.ALL, UserMoviePermission.BOX_OFFICE);
        }
    },
    SITE(Values.SITE) {
        @Override
        public Set<UserMoviePermission> getNecessaryPermissions() {
            return Sets.newHashSet(UserMoviePermission.ALL, UserMoviePermission.SITE);
        }
    },
    RELEASE_DATE(Values.RELEASE_DATE) {
        @Override
        public Set<UserMoviePermission> getNecessaryPermissions() {
            return Sets.newHashSet(UserMoviePermission.ALL, UserMoviePermission.RELEASE_DATE);
        }
    },
    OUTLINE(Values.OUTLINE) {
        @Override
        public Set<UserMoviePermission> getNecessaryPermissions() {
            return Sets.newHashSet(UserMoviePermission.ALL, UserMoviePermission.OUTLINE);
        }
    },
    SUMMARY(Values.SUMMARY) {
        @Override
        public Set<UserMoviePermission> getNecessaryPermissions() {
            return Sets.newHashSet(UserMoviePermission.ALL, UserMoviePermission.SUMMARY);
        }
    },
    SYNOPSIS(Values.SYNOPSIS) {
        @Override
        public Set<UserMoviePermission> getNecessaryPermissions() {
            return Sets.newHashSet(UserMoviePermission.ALL, UserMoviePermission.SYNOPSIS);
        }
    },
    COUNTRY(Values.COUNTRY) {
        @Override
        public Set<UserMoviePermission> getNecessaryPermissions() {
            return Sets.newHashSet(UserMoviePermission.ALL, UserMoviePermission.COUNTRY);
        }
    },
    GENRE(Values.GENRE) {
        @Override
        public Set<UserMoviePermission> getNecessaryPermissions() {
            return Sets.newHashSet(UserMoviePermission.ALL, UserMoviePermission.GENRE);
        }
    },
    LANGUAGE(Values.LANGUAGE) {
        @Override
        public Set<UserMoviePermission> getNecessaryPermissions() {
            return Sets.newHashSet(UserMoviePermission.ALL, UserMoviePermission.LANGUAGE);
        }
    },
    PHOTO(Values.PHOTO) {
        @Override
        public Set<UserMoviePermission> getNecessaryPermissions() {
            return Sets.newHashSet(UserMoviePermission.ALL, UserMoviePermission.PHOTO);
        }
    },
    POSTER(Values.POSTER) {
        @Override
        public Set<UserMoviePermission> getNecessaryPermissions() {
            return Sets.newHashSet(UserMoviePermission.ALL, UserMoviePermission.POSTER);
        }
    };

    /**
     * Value the field as a String
     */
    private String value;

    /**
     * Constructor.
     *
     * @param value Value the field as a String
     */
    MovieField(final String value) {
        this.value = value;
    }

    /**
     * The class contains field values as String.
     */
    public static class Values {
        public static final String TITLE = "TITLE";
        public static final String TYPE = "TYPE";
        public static final String OTHER_TITLE = "OTHER_TITLE";
        public static final String REVIEW = "REVIEW";
        public static final String BUDGET = "BUDGET";
        public static final String BOX_OFFICE = "BOX_OFFICE";
        public static final String SITE = "SITE";
        public static final String RELEASE_DATE = "RELEASE_DATE";
        public static final String OUTLINE = "OUTLINE";
        public static final String SUMMARY = "SUMMARY";
        public static final String SYNOPSIS = "SYNOPSIS";
        public static final String COUNTRY = "COUNTRY";
        public static final String GENRE = "GENRE";
        public static final String LANGUAGE = "LANGUAGE";
        public static final String PHOTO = "PHOTO";
        public static final String POSTER = "POSTER";
    }

    /**
     * Get a list of permissions for the field.
     *
     * @return Required permissions to edit the field
     */
    public abstract Set<UserMoviePermission> getNecessaryPermissions();
}
