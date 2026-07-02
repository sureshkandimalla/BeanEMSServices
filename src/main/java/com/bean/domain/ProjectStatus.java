package com.bean.domain;

import java.util.Arrays;

public enum ProjectStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    HOLD("Hold"),
    ONBOARDING("Onboarding");

    private final String label;

    ProjectStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }

    public static String[] labels() {
        return Arrays.stream(values()).map(ProjectStatus::getLabel).toArray(String[]::new);
    }
}
