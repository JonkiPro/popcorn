package com.jonki.popcorn.common.dto;

/**
 * Directories in the cloud to save files.
 */
public enum StorageDirectory {
    /**
     * Directory for saving photos / pictures of movies
     */
    IMAGE {
        @Override
        public String getGoogleDirectoryKey() {
            return "1_5xTX_K63I5dAMIfPecMjiIsN4tMlfvW";
        }
    },
    /**
     * Directory for saving avatars of users
     */
    AVATAR {
        @Override
        public String getGoogleDirectoryKey() {
            return "1Uz4gwteYTgWGKmJFpem0rn3WSXOiuWjd";
        }
    };

    /**
     * Get the directory key from the Google cloud.
     *
     * @return The key of the directory in the Google cloud
     */
    public abstract String getGoogleDirectoryKey();
}
