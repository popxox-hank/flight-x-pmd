package com.ctrip.flight.mobile.pmd.lang.java.rule;

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.JavaNode;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import net.sourceforge.pmd.properties.PropertyBuilder;
import net.sourceforge.pmd.properties.PropertyDescriptor;
import net.sourceforge.pmd.properties.PropertyFactory;
import net.sourceforge.pmd.util.StringUtil;

import java.util.regex.Pattern;

/**
 * @author haoren
 * Create at: 2023-07-20
 */
public abstract class AbstractFlightNamingConventionRule<T extends JavaNode>
        extends AbstractJavaRule {

    protected static final String CAMEL_CASE = "[a-z][a-zA-Z0-9]*";
    protected static final String PASCAL_CASE = "[A-Z][a-zA-Z0-9]*";

    /**
     * The argument is interpreted as the display name, and is converted to camel case to get the property name.
     */
    protected PropertyBuilder.RegexPropertyBuilder defaultProp(String displayName) {
        return defaultProp(StringUtil.toCamelCase(displayName, true), displayName);
    }

    /**
     * Returns a pre-filled builder with the given name and display name (for the description).
     */
    protected PropertyBuilder.RegexPropertyBuilder defaultProp(String name, String displayName) {
        return PropertyFactory.regexProperty(name + "Pattern")
                .desc("Regex which applies to " + displayName.trim() + " names")
                .defaultValue(defaultConvention());
    }

    /**
     * Default regex string for this kind of entities.
     */
    protected abstract String defaultConvention();


    /**
     * Generic "kind" of node, eg "static method" or "utility class".
     */
    protected abstract String kindDisplayName(T node, PropertyDescriptor<Pattern> descriptor);

    /**
     * Extracts the name that should be pattern matched.
     */
    protected String nameExtractor(T node) {
        return node.getImage();
    }


    protected abstract void checkMatches(T node, PropertyDescriptor<Pattern> regex, Object data);

    protected final void addViolationWithPrecisePosition(Object data, Node node, String message, Object... args) {
        FlightRuleUtils.addCustomViolation(this, data, node, message, args);
    }

    protected final void addViolationWithPrecisePosition(Object data, Node node, String message) {
        addViolationWithPrecisePosition(data, node, message, null);
    }

    protected final void addViolationWithPrecisePosition(Object data, Node node) {
        addViolationWithPrecisePosition(data, node, null, null);
    }
}
