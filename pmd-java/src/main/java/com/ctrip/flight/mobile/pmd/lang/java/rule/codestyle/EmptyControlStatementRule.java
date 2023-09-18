package com.ctrip.flight.mobile.pmd.lang.java.rule.codestyle;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightJavaRule;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.lang.java.rule.internal.JavaRuleUtil;

/**
 * @author haoren
 * Create at: 2023-07-20
 */
@Deprecated
public class EmptyControlStatementRule extends FlightJavaRule {

    public EmptyControlStatementRule() {
        addRuleChainVisit(ASTFinallyStatement.class);
        addRuleChainVisit(ASTSynchronizedStatement.class);
        addRuleChainVisit(ASTTryStatement.class);
        addRuleChainVisit(ASTDoStatement.class);
        addRuleChainVisit(ASTBlock.class);
        addRuleChainVisit(ASTForStatement.class);
        addRuleChainVisit(ASTWhileStatement.class);
        addRuleChainVisit(ASTIfStatement.class);
        addRuleChainVisit(ASTSwitchStatement.class);
        addRuleChainVisit(ASTInitializer.class);
    }

    @Override
    public Object visit(JavaNode node, Object data) {
        throw new UnsupportedOperationException("should not be called");
    }

    @Override
    public Object visit(ASTFinallyStatement node, Object data) {
        if (isEmpty(node.getBody())) {
            addViolationWithPrecisePosition(data, node, EMPTY_CONTROL_STATEMENT_FINALLY_VIOLATION_MSG);
        }
        return null;
    }

    @Override
    public Object visit(ASTSynchronizedStatement node, Object data) {
        if (isEmpty(node.getBody())) {
            addViolationWithPrecisePosition(data, node, EMPTY_CONTROL_STATEMENT_SYNCHRONIZED_VIOLATION_MSG);
        }
        return null;
    }

    @Override
    public Object visit(ASTSwitchStatement node, Object data) {
        if (node.getNumChildren() == 1) {
            addViolationWithPrecisePosition(data, node, EMPTY_CONTROL_STATEMENT_SWITCH_VIOLATION_MSG);
        }
        return null;
    }

    @Override
    public Object visit(ASTBlock node, Object data) {
        if (isEmpty(node) && node.getNthParent(3) instanceof ASTBlock) {
            addViolationWithPrecisePosition(data, node, EMPTY_CONTROL_STATEMENT_BLOCK_VIOLATION_MSG);
        }
        return null;
    }

    @Override
    public Object visit(ASTIfStatement node, Object data) {
        if (isEmpty(node.getThenBranch().getChild(0))) {
            addViolationWithPrecisePosition(data, node, EMPTY_CONTROL_STATEMENT_IF_VIOLATION_MSG);
        }
        if (node.hasElse() && isEmpty(node.getElseBranch().getChild(0))) {
            addViolationWithPrecisePosition(data, node, EMPTY_CONTROL_STATEMENT_ELSE_VIOLATION_MSG);
        }
        return null;
    }

    @Override
    public Object visit(ASTWhileStatement node, Object data) {
        if (isEmpty(node.getBody())) {
            addViolationWithPrecisePosition(data, node, EMPTY_CONTROL_STATEMENT_WHILE_VIOLATION_MSG);
        }
        return null;
    }

    @Override
    public Object visit(ASTForStatement node, Object data) {
        if (node.isForeach() && JavaRuleUtil.isExplicitUnusedVarName(node.getFirstChildOfType(ASTLocalVariableDeclaration.class)
                .getFirstDescendantOfType(ASTVariableDeclaratorId.class).getName())) {
            // allow `for (ignored : iterable) {}`
            return null;
        }
        if (isEmpty(node.getBody())) {
            addViolationWithPrecisePosition(data, node, EMPTY_CONTROL_STATEMENT_FOR_VIOLATION_MSG);
        }
        return null;
    }

    @Override
    public Object visit(ASTDoStatement node, Object data) {
        if (isEmpty(node.getBody())) {
            addViolationWithPrecisePosition(data, node, EMPTY_CONTROL_STATEMENT_DO_VIOLATION_MSG);
        }
        return null;
    }

    @Override
    public Object visit(ASTInitializer node, Object data) {
        if (isEmpty(node.getBody())) {
            addViolationWithPrecisePosition(data, node, EMPTY_CONTROL_STATEMENT_INITIALIZER_VIOLATION_MSG);
        }
        return null;
    }

    @Override
    public Object visit(ASTTryStatement node, Object data) {
        if (isEmpty(node.getBody())) {
            // all resources must be explicitly ignored
            boolean allResourcesIgnored = true;
            boolean hasResource = false;
            ASTResourceSpecification resources = node.getFirstChildOfType(ASTResourceSpecification.class);
            if (resources != null) {
                for (ASTResource resource : resources.findDescendantsOfType(ASTResource.class)) {
                    hasResource = true;
                    String name = resource.getStableName();
                    if (!JavaRuleUtil.isExplicitUnusedVarName(name)) {
                        allResourcesIgnored = false;
                        break;
                    }
                }
            }

            if (hasResource && !allResourcesIgnored) {
                addViolationWithPrecisePosition(data, node, EMPTY_CONTROL_STATEMENT_TRY_VIOLATION_MSG);
            } else if (!hasResource) {
                addViolationWithPrecisePosition(data, node, EMPTY_CONTROL_STATEMENT_TRY_VIOLATION_MSG);
            }
        }
        return null;
    }

    private boolean isEmpty(JavaNode node) {
        if (node instanceof ASTStatement) {
            node = node.getChild(0);
        }
        return node instanceof ASTBlock && node.getNumChildren() == 0
                || node instanceof ASTEmptyStatement;
    }

    private static final String EMPTY_CONTROL_STATEMENT_FINALLY_VIOLATION_MSG =
            "java.code.style.EmptyControlStatementRule.finally.violation.msg";

    private static final String EMPTY_CONTROL_STATEMENT_SYNCHRONIZED_VIOLATION_MSG =
            "java.code.style.EmptyControlStatementRule.synchronized.violation.msg";

    private static final String EMPTY_CONTROL_STATEMENT_SWITCH_VIOLATION_MSG =
            "java.code.style.EmptyControlStatementRule.switch.violation.msg";

    private static final String EMPTY_CONTROL_STATEMENT_BLOCK_VIOLATION_MSG =
            "java.code.style.EmptyControlStatementRule.block.violation.msg";

    private static final String EMPTY_CONTROL_STATEMENT_IF_VIOLATION_MSG =
            "java.code.style.EmptyControlStatementRule.if.violation.msg";

    private static final String EMPTY_CONTROL_STATEMENT_ELSE_VIOLATION_MSG =
            "java.code.style.EmptyControlStatementRule.else.violation.msg";

    private static final String EMPTY_CONTROL_STATEMENT_WHILE_VIOLATION_MSG =
            "java.code.style.EmptyControlStatementRule.while.violation.msg";

    private static final String EMPTY_CONTROL_STATEMENT_FOR_VIOLATION_MSG =
            "java.code.style.EmptyControlStatementRule.for.violation.msg";

    private static final String EMPTY_CONTROL_STATEMENT_DO_VIOLATION_MSG =
            "java.code.style.EmptyControlStatementRule.do.violation.msg";

    private static final String EMPTY_CONTROL_STATEMENT_INITIALIZER_VIOLATION_MSG =
            "java.code.style.EmptyControlStatementRule.initializer.violation.msg";

    private static final String EMPTY_CONTROL_STATEMENT_TRY_VIOLATION_MSG =
            "java.code.style.EmptyControlStatementRule.try.violation.msg";
}
