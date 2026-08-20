package com.employeehub.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

// Externalizes the tenant list that used to be hardcoded across
// AuthController (domain/email allowlist) and DataSourceConfig (JDBC URL per
// tenant) into application.properties/env vars — see application.properties
// for the "app.tenants[N].*" entries. Onboarding a new company is then just:
// create its database, add one app.tenants[N] entry (or the equivalent
// APP_TENANTS_<N>_* env vars on Elastic Beanstalk), and restart — no Java
// change, no rebuild, no redeploy.
@Component
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class TenantRegistryProperties {
    private List<TenantDefinition> tenants = new ArrayList<>();

    // Tenant a request resolves to when TenantContext has nothing set —
    // only happens for the JVM-local work Hibernate/Spring do outside any
    // HTTP request (e.g. schema validation at boot), never for real traffic
    // (AuthFilter always sets a tenant from the verified JWT first).
    private String defaultTenant = "intellan";
}
