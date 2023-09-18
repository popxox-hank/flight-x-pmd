package com.ctrip.flight.mobile.pmd.lang.java.rule.errorprone;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightJavaRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.*;

/**
 * @author haoren
 * Create at: 2023-08-03
 */
public class AvoidMultipleUnaryOperatorsRule extends FlightJavaRule {

    public AvoidMultipleUnaryOperatorsRule() {
        addRuleChainVisit(ASTUnaryExpression.class);
        addRuleChainVisit(ASTUnaryExpressionNotPlusMinus.class);
    }

    @Override
    public Object visit(ASTUnaryExpression node, Object data) {
        checkUnaryDescendent(node, data);
        return data;
    }

    @Override
    public Object visit(ASTUnaryExpressionNotPlusMinus node, Object data) {
        checkUnaryDescendent(node, data);
        return data;
    }

    private void checkUnaryDescendent(Node node, Object data) {
        boolean match = false;
        if (node.getNumChildren() == 1) {
            Node child = node.getChild(0);
            if (child instanceof ASTUnaryExpression || child instanceof ASTUnaryExpressionNotPlusMinus) {
                match = true;
            } else if (child instanceof ASTPrimaryExpression) {
                Node primaryExpression = child;
                // Skip down PrimaryExpression/PrimaryPrefix/Expression chains
                // created by parentheses
                while (true) {
                    if (primaryExpression.getNumChildren() == 1
                            && primaryExpression.getChild(0) instanceof ASTPrimaryPrefix
                            && primaryExpression.getChild(0).getNumChildren() == 1
                            && primaryExpression.getChild(0).getChild(0) instanceof ASTExpression
                            && primaryExpression.getChild(0).getChild(0).getNumChildren() == 1) {
                        Node candidate = primaryExpression.getChild(0).getChild(0).getChild(0);
                        if (candidate instanceof ASTUnaryExpression
                                || candidate instanceof ASTUnaryExpressionNotPlusMinus) {
                            match = true;
                            break;
                        } else if (candidate instanceof ASTPrimaryExpression) {
                            primaryExpression = candidate;
                            continue;
                        } else {
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
        }

        if (match) {
            addViolationWithPrecisePosition(data, node,AVOID_MULTIPLE_UNARY_OPERATORS_VIOLATION_MSG);
        }
    }

    private static final String AVOID_MULTIPLE_UNARY_OPERATORS_VIOLATION_MSG =
            "java.errorprone.AvoidMultipleUnaryOperatorsRule.violation.msg";
}
