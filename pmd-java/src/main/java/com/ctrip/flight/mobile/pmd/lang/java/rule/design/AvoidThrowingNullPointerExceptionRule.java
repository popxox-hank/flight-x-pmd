package com.ctrip.flight.mobile.pmd.lang.java.rule.design;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightJavaRule;
import net.sourceforge.pmd.lang.java.ast.*;

import java.util.HashSet;
import java.util.Set;

/**
 * @author haoren
 * Create at: 2023-07-24
 */
@Deprecated
public class AvoidThrowingNullPointerExceptionRule extends FlightJavaRule {

    private final Set<String> npeInstances = new HashSet<>();

    @Override
    public Object visit(ASTVariableInitializer varInitializer, Object data) {
        String initialedVarName = getInitializedVariableName(varInitializer);
        processAssignmentToVariable(varInitializer, initialedVarName);
        return super.visit(varInitializer, data);
    }

    private String getInitializedVariableName(ASTVariableInitializer initializer) {
        ASTVariableDeclaratorId varDeclaratorId = initializer.getParent()
                .getFirstDescendantOfType(ASTVariableDeclaratorId.class);
        return varDeclaratorId != null ? varDeclaratorId.getName() : null;
    }

    @Override
    public Object visit(ASTAssignmentOperator assignment, Object data) {
        String assignedVarName = getAssignedVariableName(assignment);
        processAssignmentToVariable(assignment, assignedVarName);
        return super.visit(assignment, data);
    }

    private String getAssignedVariableName(ASTAssignmentOperator assignment) {
        ASTName varName = assignment.getParent().getFirstDescendantOfType(ASTName.class);
        return varName != null ? varName.getImage() : null;
    }

    private void processAssignmentToVariable(JavaNode assignment, String varName) {
        Class<?> assignedValueType = getAssignedValueType(assignment);
        if (isNullPointerException(assignedValueType)) {
            npeInstances.add(varName);
        } else {
            npeInstances.remove(varName);
        }
    }

    private Class<?> getAssignedValueType(JavaNode assignment) {
        ASTClassOrInterfaceType assignedValueType = assignment.getParent()
                .getFirstDescendantOfType(ASTClassOrInterfaceType.class);
        return assignedValueType != null ? assignedValueType.getType() : null;
    }

    @Override
    public Object visit(ASTThrowStatement throwStatement, Object data) {
        if (throwsNullPointerException(throwStatement)) {
            addViolationWithPrecisePosition(data, throwStatement, AVOID_THROWING_NULL_POINTER_EXCEPTION_VIOLATION_MSG);
        }
        return super.visit(throwStatement, data);
    }

    private boolean throwsNullPointerException(ASTThrowStatement throwStatement) {
        return throwsNullPointerExceptionType(throwStatement)
                || throwsNullPointerExceptionVariable(throwStatement);
    }

    private boolean throwsNullPointerExceptionType(ASTThrowStatement throwStatement) {
        ASTClassOrInterfaceType thrownType = throwStatement.getFirstDescendantOfType(ASTClassOrInterfaceType.class);
        if (thrownType != null) {
            Class<?> thrownException = thrownType.getType();
            return isNullPointerException(thrownException);
        }
        return false;
    }

    private boolean isNullPointerException(Class<?> clazz) {
        return NullPointerException.class.equals(clazz);
    }

    private boolean throwsNullPointerExceptionVariable(ASTThrowStatement throwStatement) {
        ASTName thrownVar = throwStatement.getFirstDescendantOfType(ASTName.class);
        if (thrownVar != null) {
            String thrownVarName = thrownVar.getImage();
            return npeInstances.contains(thrownVarName);
        }
        return false;
    }

    private static final String AVOID_THROWING_NULL_POINTER_EXCEPTION_VIOLATION_MSG =
            "java.design.AvoidThrowingNullPointerExceptionRule.violation.msg";
}
