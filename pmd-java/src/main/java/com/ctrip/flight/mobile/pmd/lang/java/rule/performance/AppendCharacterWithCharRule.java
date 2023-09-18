package com.ctrip.flight.mobile.pmd.lang.java.rule.performance;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightJavaRule;
import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightRuleUtils;
import net.sourceforge.pmd.lang.java.ast.*;

/**
 * @author haoren
 * Create at: 2023-07-26
 */
public class AppendCharacterWithCharRule extends FlightJavaRule {

    public AppendCharacterWithCharRule() {
        addRuleChainVisit(ASTLiteral.class);
    }

    @Override
    public Object visit(ASTLiteral node, Object data) {
        ASTBlockStatement bs = node.getFirstParentOfType(ASTBlockStatement.class);
        if (bs == null) {
            return data;
        }

        if (node.isSingleCharacterStringLiteral()) {
            if (!FlightRuleUtils.isInStringBufferOperationChain(node, "append")) {
                return data;
            }

            // ignore, if the literal is part of an expression, such as "X".repeat(5)
            final ASTPrimaryExpression primaryExpression = (ASTPrimaryExpression) node.getNthParent(2);
            if (primaryExpression != null && primaryExpression.getFirstChildOfType(ASTPrimarySuffix.class) != null) {
                return data;
            }
            // ignore, if this literal is part of a different expression, e.g. "X" + something else
            if (primaryExpression != null && !(primaryExpression.getNthParent(2) instanceof ASTArgumentList)) {
                return data;
            }
            // ignore if this string literal is used as a constructor argument
            if (primaryExpression != null && primaryExpression.getNthParent(4) instanceof ASTAllocationExpression) {
                return data;
            }

            addViolationWithPrecisePosition(data, node, APPEND_CHARACTER_WITH_CHAR_VIOLATION_MSG);
        }
        return data;
    }


    private static final String APPEND_CHARACTER_WITH_CHAR_VIOLATION_MSG =
            "java.performance.AppendCharacterWithCharRule.violation.msg";
}
