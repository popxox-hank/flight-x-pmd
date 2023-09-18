package com.ctrip.flight.mobile.pmd.config;


import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * @author haoren
 * Create at: 2023-07-17
 */
class ResourceBundleContainer {
    private static final String PMD_MESSAGE_FILE_PREFIX_NAME = "pmd_message";
    private static String LANG;
    private static ResourceBundle RESOURCE_BUNDLE;
    private static final Pattern PATTERN = Pattern.compile("[\u4e00-\u9fa5]");

    ResourceBundleContainer() {
        LANG = System.getProperty("pmd.language", "zh");
        RESOURCE_BUNDLE = initialize();
    }

    private ResourceBundle initialize() {
        return ResourceBundle.getBundle(PMD_MESSAGE_FILE_PREFIX_NAME, getLocale(LANG));
    }

    String getProperty(String name, String defaultValue) {
        if (StringUtils.isEmpty(name) || !RESOURCE_BUNDLE.containsKey(name)) {
            return defaultValue;
        }

        String message = RESOURCE_BUNDLE.getString(name);
        if (PATTERN.matcher(message).find()) {
            return message;
        } else {
            return new String(message.getBytes(StandardCharsets.ISO_8859_1),
                    StandardCharsets.UTF_8);
        }
    }

    void changeLanguage(String language) {
        ResourceBundle resourceBundle = getResourceBundle(language);
        if (Objects.nonNull(resourceBundle)) {
            RESOURCE_BUNDLE = resourceBundle;
        }
    }

    void changeLanguage(Locale locale) {
        ResourceBundle resourceBundle = getResourceBundle(locale);
        if (Objects.nonNull(resourceBundle)) {
            RESOURCE_BUNDLE = resourceBundle;
        }
    }

    Locale getLanguage() {
        return Objects.nonNull(RESOURCE_BUNDLE) ? RESOURCE_BUNDLE.getLocale() : Locale.CHINESE;
    }

    /**
     * TODO 多语言支持修改成switch
     *
     * @param language
     * @return
     */
    private Locale getLocale(String language) {
        return Locale.CHINESE.getLanguage().equals(language)
                ? Locale.CHINESE
                : Locale.ENGLISH;
    }

    private ResourceBundle getResourceBundle(String language) {
        return getResourceBundle(getLocale(language));
    }

    private ResourceBundle getResourceBundle(Locale locale) {
        ResourceBundle resourceBundle;
        try {
            resourceBundle = ResourceBundle.getBundle(PMD_MESSAGE_FILE_PREFIX_NAME, locale);
        } catch (NullPointerException | MissingResourceException | IllegalArgumentException e) {
            System.out.println(e);
            resourceBundle = null;
        }
        return resourceBundle;
    }
}
