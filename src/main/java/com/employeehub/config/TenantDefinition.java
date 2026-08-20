package com.employeehub.config;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

// One row of the externalized tenant registry (see TenantRegistryProperties)
// — everything AuthController and DataSourceConfig used to have hardcoded
// per company. "domains" grants every email at that domain; "emails" grants
// specific individual addresses that can't use a domain allowlist entry
// (e.g. a personal Gmail address standing in for a company that hasn't
// handed out its own mail yet).
@Getter
@Setter
public class TenantDefinition {
    private String key;
    private List<String> domains = List.of();
    private List<String> emails = List.of();
    private String jdbcUrl;
}
