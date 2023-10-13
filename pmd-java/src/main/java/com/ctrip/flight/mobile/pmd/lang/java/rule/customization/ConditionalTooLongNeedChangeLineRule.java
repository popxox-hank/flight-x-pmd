package com.ctrip.flight.mobile.pmd.lang.java.rule.customization;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightCustomizationRule;
import net.sourceforge.pmd.lang.java.ast.ASTConditionalAndExpression;
import net.sourceforge.pmd.lang.java.ast.ASTConditionalOrExpression;
import net.sourceforge.pmd.lang.java.ast.JavaNode;
import net.sourceforge.pmd.properties.PropertyDescriptor;
import net.sourceforge.pmd.properties.PropertyFactory;

/**
 * @author haoren
 * Create at: 2023-08-25
 */
public class ConditionalTooLongNeedChangeLineRule extends FlightCustomizationRule {

    private static final PropertyDescriptor<Integer> PROBLEM_CONDITIONAL_LENGTH_LIMIT_DESCRIPTOR
            = PropertyFactory.intProperty("conditionalLengthLimit")
            .desc("This value is used to report how many characters the conditional statement needs to wrap")
            .defaultValue(80)
            .build();

    public ConditionalTooLongNeedChangeLineRule() {
        super();
        definePropertyDescriptor(PROBLEM_CONDITIONAL_LENGTH_LIMIT_DESCRIPTOR);
        addRuleChainVisit(ASTConditionalAndExpression.class);
        addRuleChainVisit(ASTConditionalOrExpression.class);
    }

    @Override
    public Object visit(ASTConditionalAndExpression node, Object data) {
        checkViolation(node, data);
        return data;
    }

    @Override
    public Object visit(ASTConditionalOrExpression node, Object data) {
        checkViolation(node, data);
        return data;
    }

    private void checkViolation(JavaNode node, Object data) {
        if (isTestClass || isTestMethod) {
            return;
        }
        final int conditionalLengthLimit = getProperty(PROBLEM_CONDITIONAL_LENGTH_LIMIT_DESCRIPTOR);
        int conditionalLength = getConditionalLength(node);
        if (conditionalLength < conditionalLengthLimit) {
            return;
        }

        if (!isChangeLine(node)) {
            addViolationWithPrecisePosition(data, node, CONDITIONAL_TOO_LONG_NEED_CHANGE_LINE_VIOLATION_MSG);
        }
    }


    private int getConditionalLength(JavaNode conditionalNode) {
        int conditionalLength = 0;
        for (JavaNode node : conditionalNode.children()) {
            conditionalLength += node.getEndColumn() - node.getBeginColumn();
        }
        return conditionalLength + 1;
    }

    private boolean isChangeLine(JavaNode conditionalNode) {
        final int baseLine = conditionalNode.getBeginLine();
        for (int i = 1; i < conditionalNode.getNumChildren(); i++) {
            if (conditionalNode.getChild(i).getBeginLine() == baseLine) {
                return false;
            }
        }
        return true;
    }

    private static final String CONDITIONAL_TOO_LONG_NEED_CHANGE_LINE_VIOLATION_MSG =
            "java.customization.ConditionalTooLongNeedChangeLineRule.violation.msg";
}
