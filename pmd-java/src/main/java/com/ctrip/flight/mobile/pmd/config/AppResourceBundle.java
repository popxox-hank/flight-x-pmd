package com.ctrip.flight.mobile.pmd.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

/**
 * @author haoren
 * Create at: 2023-07-17
 */
public class AppResourceBundle {

    private static ResourceBundleContainer RESOURCE_BUNDLE_CONTAINER = new ResourceBundleContainer();
    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

    public static void changeLanguage(String language) {
        RESOURCE_BUNDLE_CONTAINER.changeLanguage(language);
    }

    public static void changeLanguage(Locale locale) {
        RESOURCE_BUNDLE_CONTAINER.changeLanguage(locale);
    }

    public static Locale getLanguage(){
        return RESOURCE_BUNDLE_CONTAINER.getLanguage();
    }

    public static String getProperty(String name, String defaultValue) {
        return RESOURCE_BUNDLE_CONTAINER.getProperty(name, defaultValue);
    }

    public static int getIntProperty(String name, int defaultValue) {
        String value = RESOURCE_BUNDLE_CONTAINER.getProperty(name, "");
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        }
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static long getLongProperty(String name, long defaultValue) {
        String value = RESOURCE_BUNDLE_CONTAINER.getProperty(name, "");
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        }
        try {
            return Long.valueOf(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static short getShortProperty(String name, short defaultValue) {
        String value = RESOURCE_BUNDLE_CONTAINER.getProperty(name, "");
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        }
        try {
            return Short.valueOf(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static float getFloatProperty(String name, float defaultValue) {
        String value = RESOURCE_BUNDLE_CONTAINER.getProperty(name, "");
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        }
        try {
            return Float.valueOf(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static double getDoubleProperty(String name, double defaultValue) {
        String value = RESOURCE_BUNDLE_CONTAINER.getProperty(name, "");
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        }
        try {
            return Double.valueOf(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static byte getByteProperty(String name, byte defaultValue) {
        String value = RESOURCE_BUNDLE_CONTAINER.getProperty(name, "");
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        }
        try {
            return new Byte(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static boolean getBooleanProperty(String name, boolean defaultValue) {
        String value = RESOURCE_BUNDLE_CONTAINER.getProperty(name, "");
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        }
        try {
            return Boolean.valueOf(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static <T extends Enum<T>> T getEnumProperty(String name, Class<T> enumType, T defaultValue) {
        String value = RESOURCE_BUNDLE_CONTAINER.getProperty(name, "");
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        }
        try {
            return Enum.valueOf(enumType, value);
        } catch (NullPointerException | IllegalArgumentException e) {
            return defaultValue;
        }
    }

    public static Date getDateProperty(String name, Date defaultValue) {
        return getDateProperty(name, DEFAULT_DATE_PATTERN, defaultValue);
    }

    public static Date getDateProperty(String name, String format, Date defaultValue) {
        String value = RESOURCE_BUNDLE_CONTAINER.getProperty(name, "");
        if (StringUtils.isEmpty(value) || StringUtils.isEmpty(format)) {
            return defaultValue;
        }
        try {
            return FastDateFormat.getInstance(format).parse(value);
        } catch (ParseException e) {
            return defaultValue;
        }
    }

    public static Date getDateProperty(String name, Locale locale, Date defaultValue) {
        return getDateProperty(name, DEFAULT_DATE_PATTERN, locale, defaultValue);
    }

    public static Date getDateProperty(String name, String format, Locale locale, Date defaultValue) {
        String value = RESOURCE_BUNDLE_CONTAINER.getProperty(name, "");
        if (StringUtils.isEmpty(value) || StringUtils.isEmpty(format)) {
            return defaultValue;
        }
        try {
            return FastDateFormat.getInstance(format, locale).parse(value);
        } catch (ParseException e) {
            return defaultValue;
        }
    }

    public static int getIntProperty(String name) {
        return getIntProperty(name, 0);
    }

    public static long getLongProperty(String name) {
        return getLongProperty(name, 0);
    }

    public static short getShortProperty(String name) {
        return getShortProperty(name, (short) 0);
    }

    public static float getFloatProperty(String name) {
        return getFloatProperty(name, 0);
    }

    public static double getDoubleProperty(String name) {
        return getDoubleProperty(name, 0);
    }

    public static byte getByteProperty(String name) {
        return getByteProperty(name, (byte) 0);
    }

    public static boolean getBooleanProperty(String name) {
        return getBooleanProperty(name, false);
    }

    public static <T extends Enum<T>> T getEnumProperty(String name, Class<T> enumType) {
        return getEnumProperty(name, enumType, null);
    }
}

