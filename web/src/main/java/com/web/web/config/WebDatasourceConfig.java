package com.web.web.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * Database configuration.
 */
@Configuration
public class WebDatasourceConfig {

    /**
     * Get data source.
     *
     * @return Data source
     */
    @Primary
    @Bean
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource getDatasource() {
        return DataSourceBuilder.create().build();
    }
}
