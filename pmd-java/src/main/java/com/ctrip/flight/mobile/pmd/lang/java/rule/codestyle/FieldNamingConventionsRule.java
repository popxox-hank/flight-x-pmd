package com.ctrip.flight.mobile.pmd.lang.java.rule.codestyle;

import com.ctrip.flight.mobile.pmd.lang.java.rule.AbstractFlightNamingConventionRule;
import net.sourceforge.pmd.lang.java.ast.ASTEnumConstant;
import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.properties.PropertyDescriptor;
import net.sourceforge.pmd.properties.PropertyFactory;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author haoren
 * Create at: 2023-07-20
 */
public class FieldNamingConventionsRule extends AbstractFlightNamingConventionRule<ASTVariableDeclaratorId> {

    private static final PropertyDescriptor<List<String>> EXCLUDED_NAMES =
            PropertyFactory.stringListProperty("exclusions")
                    .desc("Names of fields to whitelist.")
                    .defaultValues("serialVersionUID", "serialPersistentFields")
                    .build();


    private final PropertyDescriptor<Pattern> publicConstantFieldRegex = defaultProp("public constant").defaultValue(
            "[A-Z][A-Z_0-9]*").build();
    private final PropertyDescriptor<Pattern> constantFieldRegex = defaultProp("constant").desc("Regex which applies " +
            "to non-public static final field names").defaultValue("[A-Z][A-Z_0-9]*").build();
    private final PropertyDescriptor<Pattern> enumConstantRegex = defaultProp("enum constant").defaultValue("[A-Z][A" +
            "-Z_0-9]*").build();
    private final PropertyDescriptor<Pattern> finalFieldRegex = defaultProp("final field").build();
    private final PropertyDescriptor<Pattern> staticFieldRegex = defaultProp("static field").build();
    private final PropertyDescriptor<Pattern> defaultFieldRegex = defaultProp("defaultField", "field").build();


    public FieldNamingConventionsRule() {
        definePropertyDescriptor(publicConstantFieldRegex);
        definePropertyDescriptor(constantFieldRegex);
        definePropertyDescriptor(enumConstantRegex);
        definePropertyDescriptor(finalFieldRegex);
        definePropertyDescriptor(staticFieldRegex);
        definePropertyDescriptor(defaultFieldRegex);
        definePropertyDescriptor(EXCLUDED_NAMES);

        addRuleChainVisit(ASTFieldDeclaration.class);
        addRuleChainVisit(ASTEnumConstant.class);
    }


    @Override
    public Object visit(ASTFieldDeclaration node, Object data) {
        for (ASTVariableDeclaratorId id : node) {
            if (getProperty(EXCLUDED_NAMES).contains(id.getVariableName())) {
                continue;
            }

            if (node.isFinal() && node.isStatic()) {
                checkMatches(id, node.isPublic() ? publicConstantFieldRegex : constantFieldRegex, data);
            } else if (node.isFinal()) {
                checkMatches(id, finalFieldRegex, data);
            } else if (node.isStatic()) {
                checkMatches(id, staticFieldRegex, data);
            } else {
                checkMatches(id, defaultFieldRegex, data);
            }
        }
        return data;
    }


    @Override
    public Object visit(ASTEnumConstant node, Object data) {
        if (!getProperty(enumConstantRegex).matcher(node.getImage()).matches()) {
            addViolationWithPrecisePosition(data, node, FIELD_NAMING_CONVENTIONS_VIOLATION_MSG,
                    "enum constant", node.getImage(), getProperty(enumConstantRegex).toString());
        }

        return data;
    }


    @Override
    protected String defaultConvention() {
        return CAMEL_CASE;
    }


    @Override
    protected String kindDisplayName(ASTVariableDeclaratorId node, PropertyDescriptor<Pattern> descriptor) {
        ASTFieldDeclaration field = (ASTFieldDeclaration) node.getNthParent(2);

        if (field.isFinal() && field.isStatic()) {
            return field.isPublic() ? "public constant" : "constant";
        } else if (field.isFinal()) {
            return "final field";
        } else if (field.isStatic()) {
            return "static field";
        } else {
            return "field";
        }
    }

    @Override
    protected void checkMatches(ASTVariableDeclaratorId node, PropertyDescriptor<Pattern> regex, Object data) {
        String name = nameExtractor(node);
        if (!getProperty(regex).matcher(name).matches()) {
            addViolationWithPrecisePosition(data, node, FIELD_NAMING_CONVENTIONS_VIOLATION_MSG,
                    kindDisplayName(node, regex), name, getProperty(regex).toString());
        }
    }

    private static final String FIELD_NAMING_CONVENTIONS_VIOLATION_MSG =
            "java.code.style.FieldNamingConventionsRule.violation.msg";
}
