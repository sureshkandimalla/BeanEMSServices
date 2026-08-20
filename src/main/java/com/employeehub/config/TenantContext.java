package com.employeehub.config;

// Holds the current request's resolved tenant key (one of
// TenantRegistryProperties' app.tenants[N].key entries), set by AuthFilter
// after verifying the app JWT and read by TenantRoutingDataSource to pick
// the right connection. ThreadLocal because each HTTP request is handled on
// its own thread in the standard Spring MVC (non-reactive) model this app
// uses. Tenant keys used to be hardcoded constants here — now that the
// registry is externalized (see TenantRegistryProperties), this is just the
// holder.
public final class TenantContext {

    private static final ThreadLocal<String> CURRENT = new ThreadLocal<>();

    private TenantContext() {
    }

    public static void set(String tenant) {
        CURRENT.set(tenant);
    }

    public static String get() {
        return CURRENT.get();
    }

    public static void clear() {
        CURRENT.remove();
    }
}
