package com.employeehub.config;

// Holds the current request's resolved tenant ("bean", "intellan", or
// "kkassociates"), set by AuthFilter after verifying the app JWT and read by
// TenantRoutingDataSource to pick the right connection. ThreadLocal because
// each HTTP request is handled on its own thread in the standard Spring MVC
// (non-reactive) model this app uses.
public final class TenantContext {

    public static final String BEAN = "bean";
    public static final String INTELLAN = "intellan";
    public static final String KKASSOCIATES = "kkassociates";

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
