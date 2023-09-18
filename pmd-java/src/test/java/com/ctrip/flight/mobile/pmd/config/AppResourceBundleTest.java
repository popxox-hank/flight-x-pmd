package com.ctrip.flight.mobile.pmd.config;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author haoren
 * Create at: 2023-07-17
 */

public class AppResourceBundleTest {

    @Test
    public void testGetProperty() {
        String value = AppResourceBundle.getProperty(
                "java.best.practices.AbstractClassWithoutAbstractMethod.rule.msg", "");
        Assert.assertTrue(StringUtils.isNotEmpty(value));
    }

    @Test
    public void testChangeLanguage() {
        AppResourceBundle.changeLanguage("en");
        String value = AppResourceBundle.getProperty(
                "java.best.practices.AbstractClassWithoutAbstractMethod.rule.msg", "");
        Assert.assertTrue(StringUtils.isNotEmpty(value));
    }
}
