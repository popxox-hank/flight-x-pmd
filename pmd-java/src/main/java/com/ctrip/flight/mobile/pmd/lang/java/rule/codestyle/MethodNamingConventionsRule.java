package com.ctrip.flight.mobile.pmd.lang.java.rule.codestyle;

import com.ctrip.flight.mobile.pmd.lang.java.rule.AbstractFlightNamingConventionRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.lang.java.types.TypeTestUtil;
import net.sourceforge.pmd.properties.BooleanProperty;
import net.sourceforge.pmd.properties.PropertyBuilder;
import net.sourceforge.pmd.properties.PropertyDescriptor;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author haoren
 * Create at: 2023-07-21
 */
public class MethodNamingConventionsRule extends AbstractFlightNamingConventionRule<ASTMethodDeclaration> {

    private final Map<String, String> descriptorToDisplayName = new HashMap<>();

    @Deprecated
    private static final BooleanProperty CHECK_NATIVE_METHODS_DESCRIPTOR = new BooleanProperty("checkNativeMethods",
            "deprecated! Check native methods", true, 1.0f);


    private final PropertyDescriptor<Pattern> instanceRegex = defaultProp("", "instance").build();
    private final PropertyDescriptor<Pattern> staticRegex = defaultProp("static").build();
    private final PropertyDescriptor<Pattern> nativeRegex = defaultProp("native").build();
    private final PropertyDescriptor<Pattern> junit3Regex = defaultProp("JUnit 3 test").defaultValue("test[A-Z0-9][a-zA-Z0-9]*").build();
    private final PropertyDescriptor<Pattern> junit4Regex = defaultProp("JUnit 4 test").build();
    private final PropertyDescriptor<Pattern> junit5Regex = defaultProp("JUnit 5 test").build();


    public MethodNamingConventionsRule() {
        definePropertyDescriptor(CHECK_NATIVE_METHODS_DESCRIPTOR);

        definePropertyDescriptor(instanceRegex);
        definePropertyDescriptor(staticRegex);
        definePropertyDescriptor(nativeRegex);
        definePropertyDescriptor(junit3Regex);
        definePropertyDescriptor(junit4Regex);
        definePropertyDescriptor(junit5Regex);
    }

    private boolean isJunit5Test(ASTMethodDeclaration node) {
        return node.isAnnotationPresent("org.junit.jupiter.api.Test") || node.isAnnotationPresent("org.junit.jupiter.params.ParameterizedTest");
    }

    private boolean isJunit4Test(ASTMethodDeclaration node) {
        return node.isAnnotationPresent("org.junit.Test");
    }


    private boolean isJunit3Test(ASTMethodDeclaration node) {
        if (!node.getName().startsWith("test")) {
            return false;
        }

        // Considers anonymous classes, TODO with #905 this will be easier
        Node parent = node.getFirstParentOfAnyType(ASTEnumConstant.class, ASTAllocationExpression.class, ASTAnyTypeDeclaration.class);

        if (!(parent instanceof ASTClassOrInterfaceDeclaration) || ((ASTClassOrInterfaceDeclaration) parent).isInterface()) {
            return false;
        }

        return TypeTestUtil.isA("junit.framework.TestCase", (ASTClassOrInterfaceDeclaration) parent);
    }


    @Override
    public Object visit(ASTMethodDeclaration node, Object data) {

        if (node.isAnnotationPresent("java.lang.Override")) {
            return super.visit(node, data);
        }

        if (node.isNative()) {
            if (getProperty(CHECK_NATIVE_METHODS_DESCRIPTOR)) {
                checkMatches(node, nativeRegex, data);
            } else {
                return super.visit(node, data);
            }
        } else if (node.isStatic()) {
            checkMatches(node, staticRegex, data);
        } else if (isJunit5Test(node)) {
            checkMatches(node, junit5Regex, data);
        } else if (isJunit4Test(node)) {
            checkMatches(node, junit4Regex, data);
        } else if (isJunit3Test(node)) {
            checkMatches(node, junit3Regex, data);
        } else {
            checkMatches(node, instanceRegex, data);
        }

        return super.visit(node, data);
    }


    @Override
    protected String defaultConvention() {
        return CAMEL_CASE;
    }


    @Override
    protected String nameExtractor(ASTMethodDeclaration node) {
        return node.getName();
    }


    @Override
    protected PropertyBuilder.RegexPropertyBuilder defaultProp(String name, String displayName) {
        String display = (displayName + " method").trim();
        PropertyBuilder.RegexPropertyBuilder prop = super.defaultProp(name.isEmpty() ? "method" : name, display);

        descriptorToDisplayName.put(prop.getName(), display);

        return prop;
    }


    @Override
    protected String kindDisplayName(ASTMethodDeclaration node, PropertyDescriptor<Pattern> descriptor) {
        return descriptorToDisplayName.get(descriptor.name());
    }


    @Override
    protected void checkMatches(ASTMethodDeclaration node, PropertyDescriptor<Pattern> regex, Object data) {
        String name = nameExtractor(node);
        if (!getProperty(regex).matcher(name).matches()) {
            addViolationWithPrecisePosition(data, node, METHOD_NAMING_CONVENTIONS_VIOLATION_MSG,
                    kindDisplayName(node, regex), name, getProperty(regex).toString());
        }
    }

    private static final String METHOD_NAMING_CONVENTIONS_VIOLATION_MSG =
            "java.code.style.MethodNamingConventionsRule.violation.msg";
}
