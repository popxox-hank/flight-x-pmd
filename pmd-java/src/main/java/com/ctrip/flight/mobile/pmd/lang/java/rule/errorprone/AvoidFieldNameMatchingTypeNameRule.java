package com.ctrip.flight.mobile.pmd.lang.java.rule.errorprone;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightJavaRule;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;

/**
 * @author haoren
 * Create at: 2023-08-03
 */
public class AvoidFieldNameMatchingTypeNameRule extends FlightJavaRule {

    public AvoidFieldNameMatchingTypeNameRule() {
        addRuleChainVisit(ASTFieldDeclaration.class);
    }

    @Override
    public Object visit(ASTFieldDeclaration node, Object data) {
        ASTClassOrInterfaceDeclaration cl = node.getFirstParentOfType(ASTClassOrInterfaceDeclaration.class);
        if (cl != null && node.getVariableName().equalsIgnoreCase(cl.getImage())) {
            addViolationWithPrecisePosition(data, node, AVOID_FIELD_NAME_MATCHING_TYPE_NAME_VIOLATION_MSG,
                    node.getVariableName());
        }
        return data;
    }

    private static final String AVOID_FIELD_NAME_MATCHING_TYPE_NAME_VIOLATION_MSG =
            "java.errorprone.AvoidFieldNameMatchingTypeNameRule.violation.msg";
}
