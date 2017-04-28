package com.june.easycover.utils;

public class EncodingUtils {

    public static String decodeString(String param) {
        return param.replaceAll("\\+", " ").replaceAll("%22", "").replaceAll("\"", "")
                .replaceAll("%2C", ",");
    }
    
    public static String encodeString(String param) {
        return param.replaceAll("\\+", " ").replaceAll("%22", "").replaceAll("\"", "")
                .replaceAll("%2C", ",");
    }
}
