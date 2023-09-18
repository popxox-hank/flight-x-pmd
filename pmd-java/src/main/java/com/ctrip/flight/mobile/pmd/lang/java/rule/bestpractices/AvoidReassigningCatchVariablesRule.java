package com.ctrip.flight.mobile.pmd.lang.java.rule.bestpractices;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightJavaRule;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.lang.symboltable.NameOccurrence;

/**
 * @author haoren
 * Create at: 2023-07-18
 */
@Deprecated
public class AvoidReassigningCatchVariablesRule extends FlightJavaRule {

    public AvoidReassigningCatchVariablesRule() {
        addRuleChainVisit(ASTCatchStatement.class);
    }

    @Override
    public Object visit(ASTCatchStatement catchStatement, Object data) {
        ASTVariableDeclaratorId caughtExceptionId = catchStatement.getExceptionId();
        String caughtExceptionVar = caughtExceptionId.getName();
        for (NameOccurrence usage : caughtExceptionId.getUsages()) {
            JavaNode operation = getOperationOfUsage(usage);
            if (isAssignment(operation)) {
                String assignedVar = getAssignedVariableName(operation);
                if (caughtExceptionVar.equals(assignedVar)) {
                    addViolationWithPrecisePosition(data, operation,
                            AVOID_REASSIGNING_CATCH_VARIABLES_VIOLATION_MSG, caughtExceptionVar);
                }
            }
        }
        return data;
    }

    private JavaNode getOperationOfUsage(NameOccurrence usage) {
        return usage.getLocation()
                .getFirstParentOfType(ASTPrimaryExpression.class)
                .getParent();
    }

    private boolean isAssignment(JavaNode operation) {
        return operation.hasDescendantOfType(ASTAssignmentOperator.class);
    }

    private String getAssignedVariableName(JavaNode operation) {
        return operation.getFirstDescendantOfType(ASTName.class).getImage();
    }

    private static final String AVOID_REASSIGNING_CATCH_VARIABLES_VIOLATION_MSG =
            "java.best.practices.AvoidReassigningCatchVariablesRule.violation.msg";
}
