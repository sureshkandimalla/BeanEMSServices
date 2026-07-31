package com.employeehub.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

// Picks the actual JDBC connection pool for the current request based on
// TenantContext (set by AuthFilter from the verified app JWT). Falls back
// to Intellan if a request somehow reaches here with no tenant set, rather
// than throwing — see DataSourceConfig for how the two target
// DataSources are built.
public class TenantRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        String tenant = TenantContext.get();
        return tenant != null ? tenant : TenantContext.INTELLAN;
    }
}
