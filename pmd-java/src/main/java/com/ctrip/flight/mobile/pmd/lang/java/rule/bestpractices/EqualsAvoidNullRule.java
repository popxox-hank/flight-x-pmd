package com.ctrip.flight.mobile.pmd.lang.java.rule.bestpractices;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightJavaRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.*;
import org.jaxen.JaxenException;

import java.util.List;

/**
 * @author haoren
 * Create at: 2023-07-19
 */
public class EqualsAvoidNullRule extends FlightJavaRule {

    private static final String XPATH = "//PrimaryExpression[(PrimaryPrefix[Name[(ends-with(@Image, '.equals'))]]" +
            "|PrimarySuffix[@Image='equals']|PrimaryPrefix[Name[(ends-with(@Image, '.equalsIgnoreCase'))]]" +
            "|PrimarySuffix[@Image='equalsIgnoreCase'])"
            + "[(../PrimarySuffix/Arguments/ArgumentList/Expression/PrimaryExpression/PrimaryPrefix) and "
            + "( count(../PrimarySuffix/Arguments/ArgumentList/Expression) = 1 )]]"
            + "[not(ancestor::Expression/ConditionalAndExpression//EqualityExpression[@Image='!=']//NullLiteral)]"
            + "[not(ancestor::Expression/ConditionalOrExpression//EqualityExpression[@Image='==']//NullLiteral)]";

    private static final String INVOCATION_PREFIX_XPATH
            = "PrimarySuffix/Arguments/ArgumentList/Expression/PrimaryExpression[not(PrimarySuffix)]/PrimaryPrefix";

    private static final String METHOD_EQUALS = "equals";

    @Override
    public Object visit(ASTCompilationUnit node, Object data) {
        try {
            List<Node> equalsInvocations = node.findChildNodesWithXPath(XPATH);
            if (equalsInvocations == null || equalsInvocations.isEmpty()) {
                return super.visit(node, data);
            }
            for (Node invocation : equalsInvocations) {
                // https://github.com/alibaba/p3c/issues/471
                if (callerIsLiteral(invocation)) {
                    return super.visit(node, data);
                }

                // if arguments of equals is complicate expression, skip the check
                List<? extends Node> simpleExpressions = invocation.findChildNodesWithXPath(INVOCATION_PREFIX_XPATH);
                if (simpleExpressions == null || simpleExpressions.isEmpty()) {
                    return super.visit(node, data);
                }

                ASTPrimaryPrefix right = (ASTPrimaryPrefix) simpleExpressions.get(0);
                if (right.getFirstChildOfType(ASTLiteral.class) != null) {
                    ASTLiteral literal = right.getFirstChildOfType(ASTLiteral.class);
                    if (literal.isStringLiteral()) {
                        // other literals has no equals method, can not be flipped
                        addRuleViolation(data, invocation);
                    }
                } else {
                    ASTName name = right.getFirstChildOfType(ASTName.class);
                    boolean nameInvalid = name == null || name.getNameDeclaration() == null
                            || name.getNameDeclaration().getNode() == null;
                    if (nameInvalid) {
                        return super.visit(node, data);
                    }
                    Node nameNode = name.getNameDeclaration().getNode();
                    if ((nameNode instanceof ASTVariableDeclaratorId) && (nameNode.getNthParent(
                            2) instanceof ASTFieldDeclaration)) {
                        ASTFieldDeclaration field = (ASTFieldDeclaration) nameNode.getNthParent(2);
                        if (isConstant(field)) {
                            addRuleViolation(data, invocation);
                        }
                    }
                }
            }
        } catch (JaxenException e) {
            throw new RuntimeException("XPath expression " + XPATH + " failed: " + e.getLocalizedMessage(), e);
        }
        return super.visit(node, data);
    }

    private boolean callerIsLiteral(Node equalsInvocation) {
        if (equalsInvocation instanceof ASTPrimaryExpression) {
            ASTPrimaryPrefix caller = equalsInvocation.getFirstChildOfType(ASTPrimaryPrefix.class);
            return caller != null && caller.getFirstChildOfType(ASTLiteral.class) != null;
        }
        return false;
    }

    private String getInvocationName(AbstractJavaNode javaNode) {
        Token token = (Token) javaNode.jjtGetFirstToken();
        StringBuilder sb = new StringBuilder(token.image).append(token.image);
        while (token.next != null && token.next.image != null && !METHOD_EQUALS.equals(token.next.image)) {
            token = token.next;
            sb.append(token.image);
        }
        if (sb.charAt(sb.length() - 1) == DOT) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    private void addRuleViolation(Object data, Node invocation) {
        if (invocation instanceof AbstractJavaNode) {
            AbstractJavaNode javaNode = (AbstractJavaNode) invocation;
            addViolationWithPrecisePosition(data, invocation, EQUALS_AVOID_NULL_VIOLATION_MSG,
                    getInvocationName(javaNode));
        } else {
            addViolationWithPrecisePosition(data, invocation);
        }
    }

    private boolean isConstant(ASTFieldDeclaration field) {
        return field != null && field.isStatic() && field.isFinal();
    }

    private static final char DOT = '.';
    private static final String EQUALS_AVOID_NULL_VIOLATION_MSG =
            "java.best.practices.EqualsAvoidNullRule.violation.msg";

}
