package com.ctrip.flight.mobile.pmd.lang.java.rule.customization;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightJavaRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTConditionalExpression;

import java.util.Objects;

/**
 * @author haoren
 * Create at: 2023-09-19
 */
public class AvoidTernaryNestedTernaryRule extends FlightJavaRule {

    @Override
    public Object visit(ASTConditionalExpression node, Object data) {
        if (isTernaryExpression(node) && isContainOtherTernary(node)) {
            addViolationWithPrecisePosition(data, node, AVOID_TERNARY_NESTED_TERNARY_VIOLATION_MSG);
        }
        return data;
    }

    /**
     * 三元表达式中是否包含三元表达式
     *
     * @param node
     * @return
     */
    private boolean isContainOtherTernary(ASTConditionalExpression node) {
        return isTernaryExpression(node.getTrueAlternative().getFirstChildOfType(ASTConditionalExpression.class))
                || isTernaryExpression(node.getFalseAlternative());
    }

    /**
     * 是否是三元表达式
     *
     * @param node
     * @return
     */
    private boolean isTernaryExpression(Node node) {
        if (!(node instanceof ASTConditionalExpression)) {
            return false;
        }
        ASTConditionalExpression astConditionalExpression = (ASTConditionalExpression) node;
        return Objects.nonNull(astConditionalExpression.getCondition())
                && Objects.nonNull(astConditionalExpression.getTrueAlternative())
                && Objects.nonNull(astConditionalExpression.getFalseAlternative());
    }

    private static final String AVOID_TERNARY_NESTED_TERNARY_VIOLATION_MSG =
            "java.customization.AvoidTernaryNestedTernaryRule.violation.msg";
}
