package com.core.properties;

/**
 * Stores paths to files for internationalisation.
 */
public enum BundleProperties {

    /**
     * Path to validation message file.
     */
    VALIDATION_MESSAGES("internationalization/validationMessages");

    /**
     * File path
     */
    private final String source;

    /**
     * Constructor.
     *
     * @param source File path
     */
    BundleProperties(final String source) {
        this.source = source;
    }

    /**
     * Get the file path.
     *
     * @return File path
     */
    public String getSource() {
        return source;
    }
}
