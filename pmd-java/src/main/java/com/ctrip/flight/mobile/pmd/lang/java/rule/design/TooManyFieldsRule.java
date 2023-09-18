package com.ctrip.flight.mobile.pmd.lang.java.rule.design;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightJavaRule;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.properties.PropertyDescriptor;
import net.sourceforge.pmd.properties.PropertyFactory;

import java.util.List;

import static net.sourceforge.pmd.properties.constraints.NumericConstraints.positive;

/**
 * @author haoren
 * Create at: 2023-07-25
 */
public class TooManyFieldsRule extends FlightJavaRule {

    private static final int DEFAULT_MAXFIELDS = 15;

    private static final PropertyDescriptor<Integer> MAX_FIELDS_DESCRIPTOR
            = PropertyFactory.intProperty("maxfields")
            .desc("Max allowable fields")
            .defaultValue(DEFAULT_MAXFIELDS)
            .require(positive())
            .build();

    public TooManyFieldsRule() {
        definePropertyDescriptor(MAX_FIELDS_DESCRIPTOR);
        addRuleChainVisit(ASTClassOrInterfaceDeclaration.class);
    }

    @Override
    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        final int maxFields = getProperty(MAX_FIELDS_DESCRIPTOR);
        int counter = 0;

        final List<ASTFieldDeclaration> l = node.findDescendantsOfType(ASTFieldDeclaration.class);

        for (ASTFieldDeclaration fd : l) {
            if (fd.isFinal() && fd.isStatic()) {
                continue;
            }
            counter++;
        }

        if (counter > maxFields) {
            addViolationWithPrecisePosition(data, node, TOO_MANY_FIELDS_VIOLATION_MSG, maxFields);
        }

        return data;
    }

    private static final String TOO_MANY_FIELDS_VIOLATION_MSG =
            "java.design.TooManyFieldsRule.violation.msg";
}