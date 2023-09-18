package com.ctrip.flight.mobile.pmd.lang.java.rule.codestyle;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightIgnoredAnnotationRule;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.lang.java.types.TypeTestUtil;
import net.sourceforge.pmd.properties.PropertyDescriptor;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

import static net.sourceforge.pmd.properties.PropertyFactory.booleanProperty;
import static net.sourceforge.pmd.properties.PropertyFactory.stringListProperty;

/**
 * @author haoren
 * Create at: 2023-07-21
 */
public class LinguisticNamingRule extends FlightIgnoredAnnotationRule {
    private static final PropertyDescriptor<Boolean> CHECK_BOOLEAN_METHODS =
            booleanProperty("checkBooleanMethod").defaultValue(true).desc("Check method names and types for " +
                    "inconsistent naming.").build();
    private static final PropertyDescriptor<Boolean> CHECK_GETTERS =
            booleanProperty("checkGetters").defaultValue(true).desc("Check return type of getters.").build();
    private static final PropertyDescriptor<Boolean> CHECK_SETTERS =
            booleanProperty("checkSetters").defaultValue(true).desc("Check return type of setters.").build();
    private static final PropertyDescriptor<Boolean> CHECK_PREFIXED_TRANSFORM_METHODS =
            booleanProperty("checkPrefixedTransformMethods")
                    .desc("Check return type of methods whose names start with the configured prefix (see " +
                            "transformMethodNames property).")
                    .defaultValue(true).build();
    private static final PropertyDescriptor<Boolean> CHECK_TRANSFORM_METHODS =
            booleanProperty("checkTransformMethods")
                    .desc("Check return type of methods which contain the configured infix in their name (see " +
                            "transformMethodNames property).")
                    .defaultValue(false).build();
    private static final PropertyDescriptor<Boolean> CHECK_FIELDS =
            booleanProperty("checkFields").defaultValue(true).desc("Check field names and types for inconsistent " +
                    "naming.").build();
    private static final PropertyDescriptor<Boolean> CHECK_VARIABLES =
            booleanProperty("checkVariables").defaultValue(true).desc("Check local variable names and types for " +
                    "inconsistent naming.").build();
    private static final PropertyDescriptor<List<String>> BOOLEAN_METHOD_PREFIXES_PROPERTY =
            stringListProperty("booleanMethodPrefixes")
                    .desc("The prefixes of methods that return boolean.")
                    .defaultValues("is", "has", "can", "have", "will", "should").build();
    private static final PropertyDescriptor<List<String>> TRANSFORM_METHOD_NAMES_PROPERTY =
            stringListProperty("transformMethodNames")
                    .desc("The prefixes and infixes that indicate a transform method.")
                    .defaultValues("to", "as").build();
    private static final PropertyDescriptor<List<String>> BOOLEAN_FIELD_PREFIXES_PROPERTY =
            stringListProperty("booleanFieldPrefixes")
                    .desc("The prefixes of fields and variables that indicate boolean.")
                    .defaultValues("is", "has", "can", "have", "will", "should").build();

    public LinguisticNamingRule() {
        definePropertyDescriptor(CHECK_BOOLEAN_METHODS);
        definePropertyDescriptor(CHECK_GETTERS);
        definePropertyDescriptor(CHECK_SETTERS);
        definePropertyDescriptor(CHECK_PREFIXED_TRANSFORM_METHODS);
        definePropertyDescriptor(CHECK_TRANSFORM_METHODS);
        definePropertyDescriptor(BOOLEAN_METHOD_PREFIXES_PROPERTY);
        definePropertyDescriptor(TRANSFORM_METHOD_NAMES_PROPERTY);
        definePropertyDescriptor(CHECK_FIELDS);
        definePropertyDescriptor(CHECK_VARIABLES);
        definePropertyDescriptor(BOOLEAN_FIELD_PREFIXES_PROPERTY);
        addRuleChainVisit(ASTMethodDeclaration.class);
        addRuleChainVisit(ASTFieldDeclaration.class);
        addRuleChainVisit(ASTLocalVariableDeclaration.class);
    }

    @Override
    protected Collection<String> defaultSuppressionAnnotations() {
        return Collections.checkedList(Arrays.asList("java.lang.Override"), String.class);
    }

    @Override
    public Object visit(ASTMethodDeclaration node, Object data) {
        if (!hasIgnoredAnnotation(node)) {
            String nameOfMethod = node.getName();

            if (getProperty(CHECK_BOOLEAN_METHODS)) {
                checkBooleanMethods(node, data, nameOfMethod);
            }

            if (getProperty(CHECK_SETTERS)) {
                checkSetters(node, data, nameOfMethod);
            }

            if (getProperty(CHECK_GETTERS)) {
                checkGetters(node, data, nameOfMethod);
            }

            if (getProperty(CHECK_PREFIXED_TRANSFORM_METHODS)) {
                checkPrefixedTransformMethods(node, data, nameOfMethod);
            }

            if (getProperty(CHECK_TRANSFORM_METHODS)) {
                checkTransformMethods(node, data, nameOfMethod);
            }
        }
        return data;
    }

    private void checkPrefixedTransformMethods(ASTMethodDeclaration node, Object data, String nameOfMethod) {
        ASTResultType resultType = node.getResultType();
        List<String> prefixes = getProperty(TRANSFORM_METHOD_NAMES_PROPERTY);
        String[] splitMethodName = StringUtils.splitByCharacterTypeCamelCase(nameOfMethod);
        if (resultType.isVoid() && splitMethodName.length > 0
                && prefixes.contains(splitMethodName[0].toLowerCase(Locale.ROOT))) {
            addViolationWithPrecisePosition(data, node, GENERICS_NAMING_TRANSFORM_METHODS_VIOLATION_MSG, nameOfMethod);
        }
    }

    private void checkTransformMethods(ASTMethodDeclaration node, Object data, String nameOfMethod) {
        ASTResultType resultType = node.getResultType();
        List<String> infixes = getProperty(TRANSFORM_METHOD_NAMES_PROPERTY);
        for (String infix : infixes) {
            if (resultType.isVoid() && containsWord(nameOfMethod, StringUtils.capitalize(infix))) {
                addViolationWithPrecisePosition(data, node, GENERICS_NAMING_TRANSFORM_METHODS_VIOLATION_MSG,
                        nameOfMethod);
                // the first violation is sufficient - it is still the same method we are analyzing here
                break;
            }
        }
    }

    private void checkGetters(ASTMethodDeclaration node, Object data, String nameOfMethod) {
        ASTResultType resultType = node.getResultType();
        if (hasPrefix(nameOfMethod, "get") && resultType.isVoid()) {
            addViolationWithPrecisePosition(data, node, GENERICS_NAMING_GETTERS_VIOLATION_MSG, nameOfMethod);
        }
    }

    private void checkSetters(ASTMethodDeclaration node, Object data, String nameOfMethod) {
        ASTResultType resultType = node.getResultType();
        if (hasPrefix(nameOfMethod, "set") && !resultType.isVoid()) {
            addViolationWithPrecisePosition(data, node, GENERICS_NAMING_SETTERS_VIOLATION_MSG, nameOfMethod);
        }
    }

    private boolean isBooleanType(ASTType node) {
        return "boolean" .equalsIgnoreCase(node.getTypeImage())
                || TypeTestUtil.isA("java.util.concurrent.atomic.AtomicBoolean", node)
                || TypeTestUtil.isA("java.util.function.Predicate", node);
    }

    private void checkBooleanMethods(ASTMethodDeclaration node, Object data, String nameOfMethod) {
        ASTResultType resultType = node.getResultType();
        ASTType t = node.getResultType().getFirstChildOfType(ASTType.class);
        if (!resultType.isVoid() && t != null) {
            for (String prefix : getProperty(BOOLEAN_METHOD_PREFIXES_PROPERTY)) {
                if (hasPrefix(nameOfMethod, prefix) && !isBooleanType(t)) {
                    addViolationWithPrecisePosition(data, node, GENERICS_NAMING_BOOLEAN_METHODS_VIOLATION_MSG,
                            nameOfMethod, t.getTypeImage());
                }
            }
        }
    }

    private void checkField(ASTType typeNode, ASTVariableDeclarator node, Object data) {
        for (String prefix : getProperty(BOOLEAN_FIELD_PREFIXES_PROPERTY)) {
            if (hasPrefix(node.getName(), prefix) && !isBooleanType(typeNode)) {
                addViolationWithPrecisePosition(data, node, GENERICS_NAMING_FIELD_VIOLATION_MSG, node.getName(),
                        typeNode.getTypeImage());
            }
        }
    }

    private void checkVariable(ASTType typeNode, ASTVariableDeclarator node, Object data) {
        for (String prefix : getProperty(BOOLEAN_FIELD_PREFIXES_PROPERTY)) {
            if (hasPrefix(node.getName(), prefix) && !isBooleanType(typeNode)) {
                addViolationWithPrecisePosition(data, node, GENERICS_NAMING_VARIABLE_VIOLATION_MSG, node.getName(),
                        typeNode.getTypeImage());
            }
        }
    }

    @Override
    public Object visit(ASTFieldDeclaration node, Object data) {
        ASTType type = node.getFirstChildOfType(ASTType.class);
        if (type != null && getProperty(CHECK_FIELDS)) {
            List<ASTVariableDeclarator> fields = node.findChildrenOfType(ASTVariableDeclarator.class);
            for (ASTVariableDeclarator field : fields) {
                checkField(type, field, data);
            }
        }
        return data;
    }

    @Override
    public Object visit(ASTLocalVariableDeclaration node, Object data) {
        ASTType type = node.getFirstChildOfType(ASTType.class);
        if (type != null && getProperty(CHECK_VARIABLES)) {
            List<ASTVariableDeclarator> variables = node.findChildrenOfType(ASTVariableDeclarator.class);
            for (ASTVariableDeclarator variable : variables) {
                checkVariable(type, variable, data);
            }
        }
        return data;
    }

    private static boolean hasPrefix(String name, String prefix) {
        return name.startsWith(prefix) && name.length() > prefix.length()
                && Character.isUpperCase(name.charAt(prefix.length()));
    }

    private static boolean containsWord(String name, String word) {
        int index = name.indexOf(word);
        if (index >= 0 && name.length() > index + word.length()) {
            return Character.isUpperCase(name.charAt(index + word.length()));
        }
        return false;
    }

    private static final String GENERICS_NAMING_TRANSFORM_METHODS_VIOLATION_MSG =
            "java.code.style.LinguisticNamingRule.TransformMethods.violation.msg";

    private static final String GENERICS_NAMING_GETTERS_VIOLATION_MSG =
            "java.code.style.LinguisticNamingRule.Getters.violation.msg";

    private static final String GENERICS_NAMING_SETTERS_VIOLATION_MSG =
            "java.code.style.LinguisticNamingRule.Setters.violation.msg";

    private static final String GENERICS_NAMING_BOOLEAN_METHODS_VIOLATION_MSG =
            "java.code.style.LinguisticNamingRule.BooleanMethods.violation.msg";

    private static final String GENERICS_NAMING_FIELD_VIOLATION_MSG =
            "java.code.style.LinguisticNamingRule.Field.violation.msg";

    private static final String GENERICS_NAMING_VARIABLE_VIOLATION_MSG =
            "java.code.style.LinguisticNamingRule.Variable.violation.msg";
}
