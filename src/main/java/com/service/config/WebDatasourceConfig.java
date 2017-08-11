package com.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:application.properties")
public class WebDatasourceConfig {

    // Data Source
    @Value("${spring.datasource.driver-class-name}") String databaseDriver;
    @Value("${spring.datasource.url}") String databaseUrl;
    @Value("${spring.datasource.username}") String databaseUsername;
    @Value("${spring.datasource.password}") String databasePassword;

    @Bean
    public DataSource getDatasource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(databaseDriver);
        dataSource.setUrl(databaseUrl);
        dataSource.setUsername(databaseUsername);
        dataSource.setPassword(databasePassword);

        return dataSource;
    }
}
