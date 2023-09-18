package com.ctrip.flight.mobile.pmd.lang.java.rule.customization;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightJavaRule;
import net.sourceforge.pmd.lang.java.ast.*;

/**
 * @author haoren
 * Create at: 2023-08-28
 */
public class AvoidComplexConditionalRule extends FlightJavaRule {


    public AvoidComplexConditionalRule(){
        addRuleChainVisit(ASTConditionalAndExpression.class);
        addRuleChainVisit(ASTConditionalOrExpression.class);
        addRuleChainVisit(ASTExclusiveOrExpression.class);
    }

    @Override
    public Object visit(ASTExclusiveOrExpression node, Object data) {
        if (node.findDescendantsOfType(ASTUnaryExpressionNotPlusMinus.class).size() > 0
                || node.findDescendantsOfType(ASTAndExpression.class).size() > 0
                || node.findDescendantsOfType(ASTInclusiveOrExpression.class).size() > 0
                || node.findDescendantsOfType(ASTShiftExpression.class).size() > 0) {
            addViolationWithPrecisePosition(data, node, AVOID_COMPLEX_CONDITIONAL_VIOLATION_MSG);
        }
        return data;
    }

    @Override
    public Object visit(ASTConditionalOrExpression node, Object data) {
        if (node.findDescendantsOfType(ASTConditionalAndExpression.class).size() > 0
                || hasConditionOperator(node)) {
            addViolationWithPrecisePosition(data, node, AVOID_COMPLEX_CONDITIONAL_VIOLATION_MSG);
        }
        return data;
    }


    @Override
    public Object visit(ASTConditionalAndExpression node, Object data) {
        if (node.findDescendantsOfType(ASTConditionalOrExpression.class).size() > 0
                || hasConditionOperator(node)) {
            addViolationWithPrecisePosition(data, node, AVOID_COMPLEX_CONDITIONAL_VIOLATION_MSG);
        }
        return data;
    }

    private boolean hasConditionOperator(JavaNode node) {
        return node.findDescendantsOfType(ASTUnaryExpressionNotPlusMinus.class).size() > 0
                || node.findDescendantsOfType(ASTExclusiveOrExpression.class).size() > 0
                || node.findDescendantsOfType(ASTAndExpression.class).size() > 0
                || node.findDescendantsOfType(ASTInclusiveOrExpression.class).size() > 0
                || node.findDescendantsOfType(ASTShiftExpression.class).size() > 0;
    }


    private static final String AVOID_COMPLEX_CONDITIONAL_VIOLATION_MSG =
            "java.customization.AvoidComplexConditionalRule.violation.msg";
}
