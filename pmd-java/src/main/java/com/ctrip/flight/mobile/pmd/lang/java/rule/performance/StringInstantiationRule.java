package com.ctrip.flight.mobile.pmd.lang.java.rule.performance;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightJavaRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.lang.java.symboltable.TypedNameDeclaration;
import net.sourceforge.pmd.lang.java.types.TypeTestUtil;
import net.sourceforge.pmd.lang.symboltable.NameDeclaration;

import java.util.List;

/**
 * @author haoren
 * Create at: 2023-07-26
 */
@Deprecated
public class StringInstantiationRule extends FlightJavaRule {

    public StringInstantiationRule() {
        addRuleChainVisit(ASTAllocationExpression.class);
    }

    @Override
    public Object visit(ASTAllocationExpression node, Object data) {
        if (!(node.getChild(0) instanceof ASTClassOrInterfaceType)) {
            return data;
        }

        if (!TypeTestUtil.isA(String.class, (ASTClassOrInterfaceType) node.getChild(0))) {
            return data;
        }

        if (isArrayAccess(node)) {
            addViolationWithPrecisePosition(data, node, STRING_INSTANTIATION_VIOLATION_MSG);
            return data;
        }

        List<ASTExpression> exp = node.findDescendantsOfType(ASTExpression.class);
        if (exp.size() >= 2) {
            return data;
        }

        if (node.hasDescendantOfAnyType(ASTArrayDimsAndInits.class, ASTAdditiveExpression.class)) {
            return data;
        }

        ASTName name = node.getFirstDescendantOfType(ASTName.class);
        // Literal, i.e., new String("foo")
        if (name == null) {
            addViolationWithPrecisePosition(data, node, STRING_INSTANTIATION_VIOLATION_MSG);
            return data;
        }

        NameDeclaration nd = name.getNameDeclaration();
        if (!(nd instanceof TypedNameDeclaration)) {
            return data;
        }

        if (TypeTestUtil.isA(String.class, ((TypedNameDeclaration) nd).getTypeNode())) {
            addViolationWithPrecisePosition(data, node, STRING_INSTANTIATION_VIOLATION_MSG);
        }
        return data;
    }

    private boolean isArrayAccess(ASTAllocationExpression node) {
        ASTArguments arguments = node.getFirstChildOfType(ASTArguments.class);
        if (arguments == null || arguments.size() != 1) {
            return false;
        }

        Node firstArg = arguments.getFirstChildOfType(ASTArgumentList.class).getChild(0);
        ASTPrimaryExpression primary = firstArg.getFirstChildOfType(ASTPrimaryExpression.class);
        if (primary == null || primary.getType() != String.class) {
            return false;
        }

        ASTPrimarySuffix suffix = primary.getFirstChildOfType(ASTPrimarySuffix.class);
        return suffix != null && suffix.isArrayDereference();
    }

    private static final String STRING_INSTANTIATION_VIOLATION_MSG =
            "java.performance.StringInstantiationRule.violation.msg";
}
