package com.ctrip.flight.mobile.pmd.lang.java.rule.customization;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightCustomizationRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTConditionalExpression;

import java.util.Objects;
import java.util.Optional;

/**
 * @author haoren
 * Create at: 2023-09-19
 */
public class AvoidTernaryNestedTernaryRule extends FlightCustomizationRule {

    public AvoidTernaryNestedTernaryRule() {
        super();
        addRuleChainVisit(ASTConditionalExpression.class);
    }

    @Override
    public Object visit(ASTConditionalExpression node, Object data) {
        if (isTestClass || isTestMethod) {
            return data;
        }

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
        ASTConditionalExpression astConditionalExpression;
        if (node instanceof ASTConditionalExpression) {
            astConditionalExpression = (ASTConditionalExpression) node;
        } else {
            // 处理包含的三元表达式带有括号的场景
            astConditionalExpression =
                    Optional.ofNullable(node)
                            .map(x -> x.getFirstDescendantOfType(ASTConditionalExpression.class))
                            .orElse(null);
        }
        return Objects.nonNull(astConditionalExpression)
                && Objects.nonNull(astConditionalExpression.getCondition())
                && Objects.nonNull(astConditionalExpression.getTrueAlternative())
                && Objects.nonNull(astConditionalExpression.getFalseAlternative());
    }

    private static final String AVOID_TERNARY_NESTED_TERNARY_VIOLATION_MSG =
            "java.customization.AvoidTernaryNestedTernaryRule.violation.msg";
}
