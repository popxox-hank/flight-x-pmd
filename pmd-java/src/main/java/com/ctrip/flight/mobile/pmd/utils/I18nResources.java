package com.ctrip.flight.mobile.pmd.utils;

import com.ctrip.flight.mobile.pmd.config.AppResourceBundle;
import org.apache.commons.lang3.StringUtils;

/**
 * @author haoren
 * Create at: 2023-07-17
 */
public class I18nResources {

    public static String getMessage(String key) {
        if (StringUtils.isEmpty(key)) {
            return "";
        }
        return AppResourceBundle.getProperty(key, key).trim();
    }

    public static String getMessage(String key, Object... params) {
        String value = getMessage(key);
        if (params == null || params.length == 0) {
            return value;
        }
        return String.format(value, params);
    }
}

