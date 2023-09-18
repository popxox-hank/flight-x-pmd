package com.ctrip.flight.mobile.pmd.lang.java.rule.bestpractices;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightJavaRule;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.lang.java.types.TypeTestUtil;

/**
 * @author haoren
 * Create at: 2023-07-19
 */
@Deprecated
public class PrimitiveWrapperInstantiationRule extends FlightJavaRule {

    public PrimitiveWrapperInstantiationRule() {
        addRuleChainVisit(ASTAllocationExpression.class);
        addRuleChainVisit(ASTPrimaryExpression.class);
    }

    @Override
    public Object visit(ASTAllocationExpression node, Object data) {
        if (node.getFirstChildOfType(ASTArrayDimsAndInits.class) != null) {
            return data;
        }
        ASTClassOrInterfaceType type = node.getFirstChildOfType(ASTClassOrInterfaceType.class);
        if (type == null) {
            return data;
        }

        if (TypeTestUtil.isA(Double.class, type)
                || TypeTestUtil.isA(Float.class, type)
                || TypeTestUtil.isA(Long.class, type)
                || TypeTestUtil.isA(Integer.class, type)
                || TypeTestUtil.isA(Short.class, type)
                || TypeTestUtil.isA(Byte.class, type)
                || TypeTestUtil.isA(Character.class, type)) {
            addViolationWithPrecisePosition(data, node, PRIMITIVE_WRAPPER_INSTANTIATION_VIOLATION_MSG,
                    type.getImage(), type.getImage());
        } else if (TypeTestUtil.isA(Boolean.class, type)) {
            checkArguments(node.getFirstChildOfType(ASTArguments.class), node, data);
        }

        return data;
    }

    /**
     * Finds calls of "Boolean.valueOf".
     */
    @Override
    public Object visit(ASTPrimaryExpression node, Object data) {
        if (!TypeTestUtil.isA(Boolean.class, node)) {
            return data;
        }

        if (node.getNumChildren() >= 2 && node.getChild(0).getNumChildren() > 0
                && node.getChild(0).getChild(0) instanceof ASTName
                && node.getChild(0).getChild(0).hasImageEqualTo("Boolean.valueOf")) {
            ASTPrimarySuffix suffix = (ASTPrimarySuffix) node.getChild(1);
            checkArguments(suffix.getFirstChildOfType(ASTArguments.class), node, data);
        }

        return data;
    }

    private void checkArguments(ASTArguments arguments, JavaNode node, Object data) {
        if (arguments == null || arguments.size() != 1) {
            return;
        }
        boolean isNewBoolean = node instanceof ASTAllocationExpression;
        String messagePart = isNewBoolean ? "new Boolean" : "Boolean.valueOf";
        ASTLiteral stringLiteral = getFirstArgStringLiteralOrNull(arguments);
        ASTBooleanLiteral boolLiteral = getFirstArgBooleanLiteralOrNull(arguments);
        if (stringLiteral != null) {
            if (stringLiteral.hasImageEqualTo("\"true\"")) {
                addViolationWithPrecisePosition(data, node, PRIMITIVE_WRAPPER_INSTANTIATION_BOOLEAN_VIOLATION_MSG,
                        messagePart + "(\"true\")", "Boolean.TRUE");
            } else if (stringLiteral.hasImageEqualTo("\"false\"")) {
                addViolationWithPrecisePosition(data, node, PRIMITIVE_WRAPPER_INSTANTIATION_BOOLEAN_VIOLATION_MSG,
                        messagePart + "(\"false\")", "Boolean.FALSE");
            } else {
                addViolationWithPrecisePosition(data, node, PRIMITIVE_WRAPPER_INSTANTIATION_VIOLATION_MSG,
                        "Boolean", "Boolean");
            }
        } else if (boolLiteral != null) {
            if (boolLiteral.isTrue()) {
                addViolationWithPrecisePosition(data, node, PRIMITIVE_WRAPPER_INSTANTIATION_BOOLEAN_VIOLATION_MSG,
                        messagePart + "(true)", "Boolean.TRUE");
            } else {
                addViolationWithPrecisePosition(data, node, PRIMITIVE_WRAPPER_INSTANTIATION_BOOLEAN_VIOLATION_MSG,
                        messagePart + "(false)", "Boolean.FALSE");
            }
        } else if (isNewBoolean) {
            // any argument with "new Boolean", might be a variable access
            addViolationWithPrecisePosition(data, node, PRIMITIVE_WRAPPER_INSTANTIATION_VIOLATION_MSG,
                    "Boolean", "Boolean");
        }
    }

    /**
     * <pre>
     * └─ Arguments
     *    └─ ArgumentList
     *       └─ Expression
     *          └─ PrimaryExpression
     *             └─ PrimaryPrefix
     *                └─ Literal
     * </pre>
     */
    private static ASTLiteral getFirstArgStringLiteralOrNull(ASTArguments arguments) {
        if (arguments.size() == 1) {
            ASTExpression expr = arguments.getFirstDescendantOfType(ASTExpression.class);
            ASTPrimaryExpression primaryExpr = getSingleChildOf(expr, ASTPrimaryExpression.class);
            ASTPrimaryPrefix prefix = getSingleChildOf(primaryExpr, ASTPrimaryPrefix.class);
            ASTLiteral literal = getSingleChildOf(prefix, ASTLiteral.class);
            if (literal != null && literal.isStringLiteral()) {
                return literal;
            }
        }
        return null;
    }

    /**
     * <pre>
     * └─ Arguments
     *    └─ ArgumentList
     *       └─ Expression
     *          └─ PrimaryExpression
     *             └─ PrimaryPrefix
     *                └─ Literal
     *                   └─ BooleanLiteral
     * </pre>
     */
    private static ASTBooleanLiteral getFirstArgBooleanLiteralOrNull(ASTArguments arguments) {
        if (arguments.size() == 1) {
            ASTExpression expr = arguments.getFirstDescendantOfType(ASTExpression.class);
            ASTPrimaryExpression primaryExpr = getSingleChildOf(expr, ASTPrimaryExpression.class);
            ASTPrimaryPrefix prefix = getSingleChildOf(primaryExpr, ASTPrimaryPrefix.class);
            ASTLiteral literal = getSingleChildOf(prefix, ASTLiteral.class);
            return getSingleChildOf(literal, ASTBooleanLiteral.class);
        }
        return null;
    }

    private static <N extends JavaNode> N getSingleChildOf(JavaNode node, Class<N> type) {
        if (node == null || node.getNumChildren() != 1
                || type != node.getChild(0).getClass()) {
            return null;
        }

        @SuppressWarnings("unchecked")
        N result = (N) node.getChild(0);
        return result;
    }

    private static final String PRIMITIVE_WRAPPER_INSTANTIATION_VIOLATION_MSG =
            "java.best.practices.PrimitiveWrapperInstantiationRule.violation.msg";
    private static final String PRIMITIVE_WRAPPER_INSTANTIATION_BOOLEAN_VIOLATION_MSG =
            "java.best.practices.PrimitiveWrapperInstantiationRule.Boolean.violation.msg";
}

