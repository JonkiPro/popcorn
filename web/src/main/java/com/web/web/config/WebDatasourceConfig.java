package com.web.web.config;

import com.web.web.config.properties.EclipseLinkProperties;
import com.web.web.config.properties.EntityManagerProperties;
import org.eclipse.persistence.internal.databaseaccess.DatabasePlatform;
import org.eclipse.persistence.platform.database.MySQLPlatform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.instrument.classloading.LoadTimeWeaver;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaDialect;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Database configuration.
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.core.jpa.repository")
@EntityScan(basePackages = "com.core.jpa.entity")
@EnableConfigurationProperties(value = {EclipseLinkProperties.class, EntityManagerProperties.class})
public class WebDatasourceConfig {

    @Autowired
    private EclipseLinkProperties eclipseLinkProperties;
    @Autowired
    private EntityManagerProperties entityManagerProperties;

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

    /**
     * EntityManager configuration.
     */
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(getDatasource());
        em.setJpaDialect(jpaDialect());
        em.setPackagesToScan(entityManagerProperties.getPackagesToScan());
        em.setPersistenceUnitName(eclipseLinkProperties.getPersistenceUnitName());
        final DatabasePlatform dp = new MySQLPlatform();
        em.setJpaVendorAdapter(getEclipseLinkJpaVendorAdapter());

        //following code will be used for static weaving. Uncomment when creating war.
		final Map<String, String> propMap = new HashMap<String, String>();
		propMap.put("eclipselink.weaving", eclipseLinkProperties.getWeaving());
		em.setJpaPropertyMap(propMap);

//        em.setLoadTimeWeaver(loadTimeWeaver()); //comment this when using static weaving. Mostly in development environment inside eclipse
        return em;
    }

    /**
     * Exposes EclipseLink's persistence provider and EntityManager extension interface,
     * and adapts AbstractJpaVendorAdapter's common configuration settings.
     */
    @Bean
    public EclipseLinkJpaVendorAdapter getEclipseLinkJpaVendorAdapter() {
        final EclipseLinkJpaVendorAdapter vendorAdapter = new EclipseLinkJpaVendorAdapter();
        vendorAdapter.setDatabasePlatform(eclipseLinkProperties.getDatabasePlatform());
        vendorAdapter.setGenerateDdl(eclipseLinkProperties.isGenerateDll());
        vendorAdapter.setShowSql(eclipseLinkProperties.isShowSql());
        return vendorAdapter;
    }

    /**
     * Create a new InstrumentationLoadTimeWeaver for the default ClassLoader.
     */
    @Bean()
    public LoadTimeWeaver loadTimeWeaver() {
        return new org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver();
    }

    /**
     * This is the central interface in Spring's transaction support.
     * Applications can use this directly, but it is not primarily meant as API.
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactoryBean().getObject());
        return transactionManager;
    }

    /**
     * Bean post-processor that automatically applies persistence exception translation
     * to any bean marked with Spring's @Repository annotation, adding a corresponding
     * PersistenceExceptionTranslationAdvisor to the exposed proxy
     * (either an existing AOP proxy or a newly generated proxy that implements
     * all of the target's interfaces).
     */
    @Bean
    public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    /**
     * SPI strategy that encapsulates certain functionality that standard JPA 2.1 does not offer,
     * such as access to the underlying JDBC Connection.
     * This strategy is mainly intended for standalone usage of a JPA provider.
     */
    @Bean
    public JpaDialect jpaDialect() {
        return new EclipseLinkJpaDialect();
    }
}
