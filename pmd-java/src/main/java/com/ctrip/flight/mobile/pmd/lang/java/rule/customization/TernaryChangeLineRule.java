package com.ctrip.flight.mobile.pmd.lang.java.rule.customization;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightStreamExpressionRule;
import net.sourceforge.pmd.lang.java.ast.ASTConditionalExpression;

import java.util.Objects;

/**
 * @author haoren
 * Create at: 2023-09-19
 */
public class TernaryChangeLineRule extends FlightStreamExpressionRule {

    public TernaryChangeLineRule() {
        super();
        addRuleChainVisit(ASTConditionalExpression.class);
    }

    @Override
    public Object visit(ASTConditionalExpression node, Object data) {
        if (isTestClass || isTestMethod) {
            return data;
        }

        if (isTernaryExpression(node)
                && isTernaryExpressionChangeLine(node)
                && isTernaryExpressionSameLine(node)) {
            addViolationWithPrecisePosition(data, node, TERNARY_CHANGE_LINE_VIOLATION_MSG);
        }
        return data;
    }

    /**
     * 三元表达式换行是否?和:表达式还在同一行
     *
     * @param node
     * @return
     */
    private boolean isTernaryExpressionSameLine(ASTConditionalExpression node) {
        return node.getCondition().getBeginLine() == node.getTrueAlternative().getEndLine()
                || node.getCondition().getBeginLine() == node.getFalseAlternative().getEndLine()
                || node.getTrueAlternative().getBeginLine() == node.getFalseAlternative().getEndLine();
    }

    /**
     * 三元表达式是否有换行
     *
     * @param node
     * @return
     */
    private boolean isTernaryExpressionChangeLine(ASTConditionalExpression node) {
        return node.getCondition().getBeginLine() != node.getCondition().getEndLine()
                || node.getTrueAlternative().getBeginLine() != node.getTrueAlternative().getEndLine()
                || node.getFalseAlternative().getBeginLine() != node.getFalseAlternative().getEndLine()
                || node.getCondition().getBeginLine() != node.getTrueAlternative().getEndLine()
                || node.getCondition().getBeginLine() != node.getFalseAlternative().getEndLine()
                || node.getTrueAlternative().getBeginLine() != node.getFalseAlternative().getEndLine();
    }

    /**
     * 是否是三元表达式
     *
     * @param node
     * @return
     */
    private boolean isTernaryExpression(ASTConditionalExpression node) {
        return Objects.nonNull(node.getCondition())
                && Objects.nonNull(node.getTrueAlternative())
                && Objects.nonNull(node.getFalseAlternative());
    }

    private static final String TERNARY_CHANGE_LINE_VIOLATION_MSG =
            "java.customization.TernaryChangeLineRule.violation.msg";
}
