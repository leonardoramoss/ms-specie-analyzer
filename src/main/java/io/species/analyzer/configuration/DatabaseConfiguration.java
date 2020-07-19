package io.species.analyzer.configuration;

import io.species.analyzer.domain.species.SpeciesAnalysisRepository;
import org.eclipse.persistence.config.BatchWriting;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackageClasses = { SpeciesAnalysisRepository.class })
public class DatabaseConfiguration extends JpaBaseConfiguration {

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
        final var vendorProperties = new HashMap<String, Object>();

        vendorProperties.put(PersistenceUnitProperties.WEAVING, "static");
        vendorProperties.put(PersistenceUnitProperties.QUERY_CACHE, TRUE);
        vendorProperties.put(PersistenceUnitProperties.THROW_EXCEPTIONS, TRUE);
        vendorProperties.put(PersistenceUnitProperties.BATCH_WRITING, BatchWriting.JDBC);
        vendorProperties.put(PersistenceUnitProperties.CACHE_STATEMENTS, FALSE);
        vendorProperties.put(PersistenceUnitProperties.CACHE_SHARED_DEFAULT, FALSE);
        vendorProperties.put(PersistenceUnitProperties.PERSISTENCE_CONTEXT_FLUSH_MODE, "commit");
        vendorProperties.put(PersistenceUnitProperties.PERSISTENCE_CONTEXT_CLOSE_ON_COMMIT, TRUE);
        vendorProperties.put(PersistenceUnitProperties.PERSISTENCE_CONTEXT_PERSIST_ON_COMMIT, FALSE);

        return Collections.unmodifiableMap(vendorProperties);
    }
}
