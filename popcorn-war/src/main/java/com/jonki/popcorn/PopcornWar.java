package com.jonki.popcorn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * A class that serves to set up Spring Boot within a servlet container rather than an embedded one.
 */
@SpringBootApplication
public class PopcornWar extends SpringBootServletInitializer {

    /**
     * Spring Boot Main.
     *
     * @param args Program arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(PopcornWeb.class, args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        return application.sources(PopcornWeb.class);
    }
}
