package com.june.easycover.utils;

public class EncodingUtils {

    public static String decode(String paramValue) {
        return paramValue.replaceAll("\\+", " ").replaceAll("%22", "").replaceAll("\"", "")
                .replaceAll("%2C", ",");
    }
}
