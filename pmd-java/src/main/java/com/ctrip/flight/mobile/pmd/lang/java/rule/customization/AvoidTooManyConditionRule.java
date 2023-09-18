package com.ctrip.flight.mobile.pmd.lang.java.rule.customization;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightJavaRule;
import net.sourceforge.pmd.lang.java.ast.ASTConditionalAndExpression;
import net.sourceforge.pmd.lang.java.ast.ASTConditionalOrExpression;
import net.sourceforge.pmd.properties.PropertyDescriptor;
import net.sourceforge.pmd.properties.PropertyFactory;

/**
 * @author haoren
 * Create at: 2023-09-13
 */
public class AvoidTooManyConditionRule extends FlightJavaRule {

    private static final PropertyDescriptor<Integer> PROBLEM_CONDITION_LIMIT_NUM_DESCRIPTOR
            = PropertyFactory.intProperty("conditionLimitedNum")
            .desc("this is reports the number of condition")
            .defaultValue(4)
            .build();

    public AvoidTooManyConditionRule() {
        definePropertyDescriptor(PROBLEM_CONDITION_LIMIT_NUM_DESCRIPTOR);
        addRuleChainVisit(ASTConditionalOrExpression.class);
        addRuleChainVisit(ASTConditionalAndExpression.class);
    }

    @Override
    public Object visit(ASTConditionalOrExpression node, Object data) {
        final int conditionLimitedNum = getProperty(PROBLEM_CONDITION_LIMIT_NUM_DESCRIPTOR);
        if (node.getNumChildren() > conditionLimitedNum) {
            addViolationWithPrecisePosition(data, node, AVOID_TOO_MANY_CONDITION_VIOLATION_MSG, conditionLimitedNum);
        }
        return data;
    }

    @Override
    public Object visit(ASTConditionalAndExpression node, Object data) {
        final int conditionLimitedNum = getProperty(PROBLEM_CONDITION_LIMIT_NUM_DESCRIPTOR);
        if (node.getNumChildren() > conditionLimitedNum) {
            addViolationWithPrecisePosition(data, node, AVOID_TOO_MANY_CONDITION_VIOLATION_MSG, conditionLimitedNum);
        }
        return data;
    }

    private static final String AVOID_TOO_MANY_CONDITION_VIOLATION_MSG =
            "java.customization.AvoidTooManyConditionRule.violation.msg";
}
