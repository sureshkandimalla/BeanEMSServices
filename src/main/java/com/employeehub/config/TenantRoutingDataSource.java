package com.employeehub.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

// Picks the actual JDBC connection pool for the current request based on
// TenantContext (set by AuthFilter from the verified app JWT). Falls back to
// the registry's configured default tenant if a request somehow reaches
// here with no tenant set, rather than throwing — see DataSourceConfig for
// how the target DataSources are built from TenantRegistryProperties.
public class TenantRoutingDataSource extends AbstractRoutingDataSource {

    private final String defaultTenant;

    public TenantRoutingDataSource(String defaultTenant) {
        this.defaultTenant = defaultTenant;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        String tenant = TenantContext.get();
        return tenant != null ? tenant : defaultTenant;
    }
}
