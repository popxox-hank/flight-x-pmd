package com.ctrip.flight.mobile.pmd.lang.java.rule.performance;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightJavaRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.lang.java.types.TypeTestUtil;

import java.util.Collection;

/**
 * @author haoren
 * Create at: 2023-07-26
 */
public class AvoidInstantiatingObjectsInLoopsRule extends FlightJavaRule {

    public AvoidInstantiatingObjectsInLoopsRule() {
        addRuleChainVisit(ASTAllocationExpression.class);
    }

    /**
     * This method is used to check whether user instantiates variables
     * which are not assigned to arrays/lists in loops.
     *
     * @param node This is the expression of part of java code to be checked.
     * @param data This is the data to return.
     * @return Object This returns the data passed in. If violation happens, violation is added to data.
     */
    @Override
    public Object visit(ASTAllocationExpression node, Object data) {
        if (notInsideLoop(node)) {
            return data;
        }

        if (fourthParentNotThrow(node)
                && fourthParentNotReturn(node)
                && notArrayAssignment(node)
                && notCollectionAccess(node)
                && notBreakFollowing(node)) {
            addViolationWithPrecisePosition(data, node, AVOID_INSTANTIATING_OBJECTS_IN_LOOPS_VIOLATION_MSG);
        }
        return data;
    }

    private boolean notArrayAssignment(ASTAllocationExpression node) {
        if (node.getNthParent(4) instanceof ASTStatementExpression) {
            ASTPrimaryExpression assignee = node.getNthParent(4).getFirstChildOfType(ASTPrimaryExpression.class);
            ASTPrimarySuffix suffix = assignee.getFirstChildOfType(ASTPrimarySuffix.class);
            return suffix == null || !suffix.isArrayDereference();
        }
        return true;
    }

    private boolean notCollectionAccess(ASTAllocationExpression node) {
        if (node.getNthParent(4) instanceof ASTArgumentList && node.getNthParent(8) instanceof ASTStatementExpression) {
            ASTStatementExpression statement = (ASTStatementExpression) node.getNthParent(8);
            return !isCallOnReceiverOfType(Collection.class, statement);
        }
        return true;
    }

    private static boolean isCallOnReceiverOfType(Class<?> receiverType, JavaNode expression) {
        if ((expression instanceof ASTExpression || expression instanceof ASTStatementExpression)
                && expression.getNumChildren() == 1) {
            expression = expression.getChild(0);
        }
        int numChildren = expression.getNumChildren();
        if (expression instanceof ASTPrimaryExpression && numChildren >= 2) {
            JavaNode lastChild = expression.getChild(numChildren - 1);
            if (lastChild instanceof ASTPrimarySuffix && ((ASTPrimarySuffix) lastChild).isArguments()) {
                JavaNode receiverExpr = expression.getChild(numChildren - 2);
                return receiverExpr instanceof TypeNode && TypeTestUtil.isA(receiverType, (TypeNode) receiverExpr);
            }
        }
        return false;
    }

    private boolean notBreakFollowing(ASTAllocationExpression node) {
        ASTBlockStatement blockStatement = node.getFirstParentOfType(ASTBlockStatement.class);
        if (blockStatement != null) {
            ASTBlock block = blockStatement.getFirstParentOfType(ASTBlock.class);
            if (block.getNumChildren() > blockStatement.getIndexInParent() + 1) {
                ASTBlockStatement next = (ASTBlockStatement) block.getChild(blockStatement.getIndexInParent() + 1);
                if (next.getNumChildren() == 1 && next.getChild(0).getNumChildren() == 1) {
                    return !(next.getChild(0).getChild(0) instanceof ASTBreakStatement);
                }
            }
        }
        return true;
    }

    /**
     * This method is used to check whether this expression is a throw statement.
     *
     * @param node This is the expression of part of java code to be checked.
     * @return boolean This returns Whether the fourth parent of node is an instance of throw statement.
     */
    private boolean fourthParentNotThrow(ASTAllocationExpression node) {
        return !(node.getNthParent(4) instanceof ASTThrowStatement);
    }

    /**
     * This method is used to check whether this expression is a return statement.
     *
     * @param node This is the expression of part of java code to be checked.
     * @return boolean This returns Whether the fourth parent of node is an instance of return statement.
     */
    private boolean fourthParentNotReturn(ASTAllocationExpression node) {
        return !(node.getNthParent(4) instanceof ASTReturnStatement);
    }

    /**
     * This method is used to check whether this expression is not in a loop.
     *
     * @param node This is the expression of part of java code to be checked.
     * @return boolean <code>false</code> if the given node is inside a loop, <code>true</code> otherwise
     */
    private boolean notInsideLoop(ASTAllocationExpression node) {
        Node n = node.getParent();
        while (n != null) {
            if (n instanceof ASTDoStatement || n instanceof ASTWhileStatement || n instanceof ASTForStatement) {
                return false;
            } else if (n instanceof ASTForInit) {
                n = n.getParent();
            } else if (n.getParent() instanceof ASTForStatement && n.getParent().getNumChildren() > 1
                    && n == n.getParent().getChild(1)) {
                n = n.getParent();
            }
            n = n.getParent();
        }
        return true;
    }

    private static final String AVOID_INSTANTIATING_OBJECTS_IN_LOOPS_VIOLATION_MSG =
            "java.performance.AvoidInstantiatingObjectsInLoopsRule.violation.msg";
}
