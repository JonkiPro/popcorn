package com.web.web.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Properties for EclipseLink configuration.
 */
@ConfigurationProperties(prefix = "eclipse-link")
@Getter
@Setter
@Validated
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
