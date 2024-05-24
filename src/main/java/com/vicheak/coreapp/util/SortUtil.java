package com.vicheak.coreapp.util;

public enum SortUtil {

    DIRECTION("_direction"),
    FIELD("_field");

    public final String label;

    SortUtil(String label) {
        this.label = label;
    }

}
