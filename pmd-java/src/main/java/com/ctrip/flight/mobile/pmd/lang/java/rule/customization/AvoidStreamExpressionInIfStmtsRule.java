package com.ctrip.flight.mobile.pmd.lang.java.rule.customization;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightStreamExpressionRule;
import net.sourceforge.pmd.lang.java.ast.ASTExpression;
import net.sourceforge.pmd.lang.java.ast.ASTIfStatement;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryExpression;
import net.sourceforge.pmd.lang.java.ast.JavaNode;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author haoren
 * Create at: 2023-08-25
 */
public class AvoidStreamExpressionInIfStmtsRule extends FlightStreamExpressionRule {

    private boolean isContainStream;

    public AvoidStreamExpressionInIfStmtsRule() {
        super();
        addRuleChainVisit(ASTPrimaryExpression.class);
        addRuleChainVisit(ASTIfStatement.class);
    }

    @Override
    public Object visit(ASTPrimaryExpression node, Object data) {
        setLocalStreamVariableName(node);
        return data;
    }

    @Override
    public Object visit(ASTIfStatement node, Object data) {
        if (isTestClass || isTestMethod) {
            return data;
        }

        if (isContainStreamExpression(node)) {
            addViolationWithPrecisePosition(data, node, AVOID_STREAM_EXPRESSION_IN_IF_STMTS_VIOLATION_MSG);
        }
        return data;
    }

    private boolean isContainStreamExpression(ASTIfStatement node) {
        isContainStream = false;
        for (ASTExpression astExpression : node.findChildrenOfType(ASTExpression.class)) {
            loopCheckStreamExpression(astExpression);
        }
        return isContainStream;
    }

    private void loopCheckStreamExpression(JavaNode node) {
        if (isContainStream
                || Objects.isNull(node)
                || isSpecialStreamExpression(node)
                || unCheckViolation(node.getFirstChildOfType(ASTPrimaryExpression.class))) {
            return;
        }

        String imageName;
        for (JavaNode childNode : node.children()) {
            imageName = getPrimaryExpressionImageName(childNode);
            if (StringUtils.isNotEmpty(imageName) && isStream(imageName)) {
                isContainStream = true;
                return;
            }
            loopCheckStreamExpression(childNode);
        }
    }

    private static final String AVOID_STREAM_EXPRESSION_IN_IF_STMTS_VIOLATION_MSG =
            "java.customization.AvoidStreamExpressionInIfStmtsRule.violation.msg";
}
