package com.jonki.popcorn.common.dto.movie.type;

/**
 * Countries of production of the movie.
 */
public enum CountryType {
    USA {
        @Override
        public String getCode() {
            return "US";
        }
    },
    POLAND {
        @Override
        public String getCode() {
            return "PL";
        }
    };

    /**
     * Get a list of permissions for the field.
     *
     * @return Required permissions to edit the field
     */
    public abstract String getCode();
}
