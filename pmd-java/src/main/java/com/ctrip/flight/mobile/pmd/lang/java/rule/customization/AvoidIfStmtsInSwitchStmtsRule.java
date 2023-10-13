package com.ctrip.flight.mobile.pmd.lang.java.rule.customization;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightCustomizationRule;
import net.sourceforge.pmd.lang.java.ast.ASTIfStatement;
import net.sourceforge.pmd.lang.java.ast.ASTSwitchStatement;

/**
 * @author haoren
 * Create at: 2023-08-30
 */
public class AvoidIfStmtsInSwitchStmtsRule extends FlightCustomizationRule {

    public AvoidIfStmtsInSwitchStmtsRule() {
        super();
        addRuleChainVisit(ASTSwitchStatement.class);
    }

    @Override
    public Object visit(ASTSwitchStatement node, Object data) {
        if (isTestClass || isTestMethod) {
            return data;
        }

        if (node.findDescendantsOfType(ASTIfStatement.class).size() > 0) {
            addViolationWithPrecisePosition(data, node, AVOID_IF_STMTS_IN_SWITCH_STMTS_VIOLATION_MSG);
        }
        return data;
    }


    private static final String AVOID_IF_STMTS_IN_SWITCH_STMTS_VIOLATION_MSG =
            "java.customization.AvoidIfStmtsInSwitchStmtsRule.violation.msg";
}
