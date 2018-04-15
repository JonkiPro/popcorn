package com.web.web.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Properties for EntityManager configuration.
 */
@ConfigurationProperties(prefix = "entity-manager")
@Getter
@Setter
@Validated
public class EntityManagerProperties {

    /**
     * Packages to scan
     */
    private String packagesToScan;
}
