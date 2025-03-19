package org.mobile.utils;

public class StringUtil {

    public static String formatedStringWithArgs(String preUpdatedValue, Object... args) {
        if (args.length > 0) {
            preUpdatedValue = preUpdatedValue.formatted(args);
            return preUpdatedValue;
        } else {
            throw new IllegalArgumentException("No arguments provided for the locator: " + preUpdatedValue);
        }
    }
}
