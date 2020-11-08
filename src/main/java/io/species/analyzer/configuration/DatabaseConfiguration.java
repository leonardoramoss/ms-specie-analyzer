package io.species.analyzer.configuration;

import io.species.analyzer.domain.species.SpecieAnalysisCounterRepository;
import io.species.analyzer.domain.species.SpeciesAnalysisRepository;
import org.eclipse.persistence.config.BatchWriting;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.instrument.classloading.LoadTimeWeaver;
import org.springframework.instrument.classloading.ReflectiveLoadTimeWeaver;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties(JpaProperties.class)
@EnableJpaRepositories(basePackageClasses = { SpeciesAnalysisRepository.class, SpecieAnalysisCounterRepository.class})
public class DatabaseConfiguration extends JpaBaseConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseConfiguration.class);

    private static final String TRUE = "true";
    private static final String FALSE = "false";

    protected DatabaseConfiguration(final DataSource dataSource,
                                    final JpaProperties properties,
                                    final ObjectProvider<JtaTransactionManager> jtaTransactionManager) {
        super(dataSource, properties, jtaTransactionManager);
    }

    @Override
    protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
        return new EclipseLinkJpaVendorAdapter();
    }

    @Override
    protected Map<String, Object> getVendorProperties() {
        return Map.of(
                PersistenceUnitProperties.WEAVING, detectWeavingMode(),
                PersistenceUnitProperties.QUERY_CACHE, TRUE,
                PersistenceUnitProperties.THROW_EXCEPTIONS, TRUE,
                PersistenceUnitProperties.BATCH_WRITING, BatchWriting.JDBC,
                PersistenceUnitProperties.CACHE_STATEMENTS, FALSE,
                PersistenceUnitProperties.CACHE_SHARED_DEFAULT, FALSE,
                PersistenceUnitProperties.PERSISTENCE_CONTEXT_FLUSH_MODE, "commit",
                PersistenceUnitProperties.PERSISTENCE_CONTEXT_CLOSE_ON_COMMIT, TRUE,
                PersistenceUnitProperties.PERSISTENCE_CONTEXT_PERSIST_ON_COMMIT, FALSE,
                PersistenceUnitProperties.LOGGING_LEVEL, Level.SEVERE.getName()
        );
    }

    private String detectWeavingMode() {
        LOGGER.info("WEAVING MODE: " + (InstrumentationLoadTimeWeaver.isInstrumentationAvailable() ? "true" : "static"));
        return InstrumentationLoadTimeWeaver.isInstrumentationAvailable() ? "true" : "static";
    }
}
