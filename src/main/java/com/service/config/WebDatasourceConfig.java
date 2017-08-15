package com.service.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class WebDatasourceConfig {

    @Primary
    @Bean
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource getDatasource() {
        return DataSourceBuilder.create().build();
    }
}
