package com.amazon.sampleapp;

import com.google.gson.Gson;

import java.io.Reader;

public class GsonUtils {

    private static Gson gson = null;

    private GsonUtils() {
    }

    /**
     *
     * @param object
     * @return
     */
    public static String toJsonString(Object object) {
        if (gson == null) {
            gson = new Gson();
        }
        return gson.toJson(object);
    }

    public static <T> String toJsonString(Object object, Class<T> cls) {
        if (gson == null) {
            gson = new Gson();
        }
        return gson.toJson(object, cls);
    }

    public static <T> T toObject(Reader reader, Class<T> cls) {
        if (gson == null) {
            gson = new Gson();
        }
        return gson.fromJson(reader, cls);
    }
}
