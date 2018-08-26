package com.jonki.popcorn.core;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Spring configuration class for integration tests.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
@EnableJpaAuditing
public class PopcornCoreTestApplication {
}
