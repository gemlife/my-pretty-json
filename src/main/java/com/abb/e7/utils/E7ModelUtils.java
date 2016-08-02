package com.abb.e7.utils;

import static org.json.JSONObject.NULL;

public class E7ModelUtils {

    private E7ModelUtils() {

    }

    public static Object requiredNullableValueOf(String nullableValue) {
        return nullableValue != null ? nullableValue : NULL;
    }

}
