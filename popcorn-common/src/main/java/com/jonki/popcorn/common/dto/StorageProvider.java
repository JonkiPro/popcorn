package com.jonki.popcorn.common.dto;

/**
 * Cloud service providers for saving files.
 */
public enum StorageProvider {
    /**
     * Provider for Google cloud
     */
    GOOGLE {
        @Override
        public String getUrlFile(final String id) {
            return "https://drive.google.com/uc?id=" + id;
        }
    };

    /**
     * Get the file address (URL).
     *
     * @param id The file ID in the cloud
     * @return Address (URL) of the file
     */
    public abstract String getUrlFile(final String id);
}
