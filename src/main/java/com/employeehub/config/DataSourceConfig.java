package com.employeehub.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

// Replaces Spring Boot's single auto-configured DataSource with one that
// routes each request to the right tenant's schema based on TenantContext.
// The tenant list itself is externalized (see TenantRegistryProperties) —
// adding a company here is just one more app.tenants[N] entry, no code
// change. Defining this @Bean makes Spring Boot's own
// DataSourceAutoConfiguration back off (it's @ConditionalOnMissingBean), so
// no exclusion/property juggling is needed beyond this class.
@Configuration
public class DataSourceConfig {

    @Autowired
    private TenantRegistryProperties tenantRegistry;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    @Primary
    public DataSource dataSource() {
        Map<Object, Object> targetDataSources = new HashMap<>();
        DataSource defaultDataSource = null;

        for (TenantDefinition tenant : tenantRegistry.getTenants()) {
            DataSource ds = buildDataSource(tenant.getJdbcUrl());
            targetDataSources.put(tenant.getKey(), ds);
            if (tenant.getKey().equals(tenantRegistry.getDefaultTenant())) {
                defaultDataSource = ds;
            }
        }

        if (defaultDataSource == null && !targetDataSources.isEmpty()) {
            // Configured default-tenant key didn't match any registry entry —
            // fall back to whichever tenant loaded first rather than leaving
            // the routing DataSource with no default at all.
            defaultDataSource = (DataSource) targetDataSources.values().iterator().next();
        }

        TenantRoutingDataSource routingDataSource = new TenantRoutingDataSource(tenantRegistry.getDefaultTenant());
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(defaultDataSource);
        routingDataSource.afterPropertiesSet();
        return routingDataSource;
    }

    private DataSource buildDataSource(String url) {
        return DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
    }
}
