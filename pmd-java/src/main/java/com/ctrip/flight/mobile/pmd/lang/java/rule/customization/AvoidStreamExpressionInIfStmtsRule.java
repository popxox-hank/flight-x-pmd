package com.ctrip.flight.mobile.pmd.lang.java.rule.customization;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightStreamExpressionRule;
import net.sourceforge.pmd.lang.java.ast.ASTExpression;
import net.sourceforge.pmd.lang.java.ast.ASTIfStatement;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryExpression;
import net.sourceforge.pmd.lang.java.ast.JavaNode;

import java.util.List;
import java.util.stream.Collectors;

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
        setContainStreamVariableName(node);
        return data;
    }

    @Override
    public Object visit(ASTIfStatement node, Object data) {
        if (isContainStreamExpression(node)) {
            addViolationWithPrecisePosition(data, node, AVOID_STREAM_EXPRESSION_IN_IF_STMTS_VIOLATION_MSG);
        }
        return data;
    }

    private boolean isContainStreamExpression(ASTIfStatement node) {
        List<ASTExpression> expressionList = node.findChildrenOfType(ASTExpression.class);
        if (expressionList.isEmpty()) {
            return false;
        }
        isContainStream = false;
        for (int i = 0; i < expressionList.size(); i++) {
            loopCheckStreamExpression(expressionList.get(i));
        }

        return isContainStream;
    }

    private void loopCheckStreamExpression(JavaNode node) {
        if (isSpecialStreamExpression(node)) {
            return;
        }
        if (isContainStream) {
            return;
        }
        if (unCheckViolation(node.getFirstChildOfType(ASTPrimaryExpression.class))) {
            return;
        }
        String imageName;
        for (int i = 0; i < node.getNumChildren(); i++) {
            imageName = getPrimaryExpressionImageName(node.getChild(i));
            if (isStream(imageName)) {
                isContainStream = true;
                return;
            }
        }
        for (int i = 0; i < node.getNumChildren(); i++) {
            loopCheckStreamExpression(node.getChild(i));
        }
    }

    private static final String AVOID_STREAM_EXPRESSION_IN_IF_STMTS_VIOLATION_MSG =
            "java.customization.AvoidStreamExpressionInIfStmtsRule.violation.msg";
}
