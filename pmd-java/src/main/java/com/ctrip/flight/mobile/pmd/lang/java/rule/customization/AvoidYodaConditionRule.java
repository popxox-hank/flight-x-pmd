package com.ctrip.flight.mobile.pmd.lang.java.rule.customization;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightJavaRule;
import net.sourceforge.pmd.lang.java.ast.ASTEqualityExpression;
import net.sourceforge.pmd.lang.java.ast.ASTLiteral;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTNullLiteral;

import java.util.Objects;

/**
 * @author haoren
 * Create at: 2023-09-19
 */
public class AvoidYodaConditionRule extends FlightJavaRule {

    private static final Integer DEFAULT_LENGTH = 2;

    @Override
    public Object visit(ASTEqualityExpression node, Object data) {
        if (node.getNumChildren() == DEFAULT_LENGTH && isYodaCondition(node)) {
            addViolationWithPrecisePosition(data, node, AVOID_YODA_CONDITION_VIOLATION_MSG);
        }
        return data;
    }

    private boolean isYodaCondition(ASTEqualityExpression node) {
        boolean isRightName = Objects.nonNull(node.getChild(1).getFirstDescendantOfType(ASTName.class));
        boolean isLeftLiteral = Objects.nonNull(node.getChild(0).getFirstDescendantOfType(ASTLiteral.class))
                || Objects.nonNull(node.getChild(0).getFirstDescendantOfType(ASTNullLiteral.class));
        boolean isUnLeftName = Objects.isNull(node.getChild(0).getFirstDescendantOfType(ASTName.class));
        return isLeftLiteral && isUnLeftName && isRightName;
    }

    private static final String AVOID_YODA_CONDITION_VIOLATION_MSG =
            "java.customization.AvoidYodaConditionRule.violation.msg";

}
