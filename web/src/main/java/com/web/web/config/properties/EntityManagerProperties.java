package com.web.web.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties for EntityManager configuration.
 */
@Data
@ConfigurationProperties("entityManager")
public class EntityManagerProperties {

    /**
     * Packages to scan
     */
    private String packagesToScan;
}
