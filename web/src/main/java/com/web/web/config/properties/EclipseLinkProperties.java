package com.web.web.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties for EclipseLink configuration.
 */
@Data
@ConfigurationProperties("eclipseLink")
public class EclipseLinkProperties {

    /**
     * Persistence unit name
     */
    private String persistenceUnitName;

    /**
     * Database platform
     */
    private String databasePlatform;

    /**
     * Generate DLL
     */
    private boolean generateDll;

    /**
     * Show SQL
     */
    private boolean showSql;

    /**
     * Weaving
     */
    private String weaving;
}
