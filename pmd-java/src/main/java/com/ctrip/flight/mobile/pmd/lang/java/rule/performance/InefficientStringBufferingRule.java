package com.ctrip.flight.mobile.pmd.lang.java.rule.performance;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightJavaRule;
import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightRuleUtils;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.lang.java.symboltable.VariableNameDeclaration;
import net.sourceforge.pmd.lang.java.types.TypeTestUtil;

import java.util.Iterator;
import java.util.List;

/**
 * @author haoren
 * Create at: 2023-07-26
 */
public class InefficientStringBufferingRule extends FlightJavaRule {

    public InefficientStringBufferingRule() {
        addRuleChainVisit(ASTAdditiveExpression.class);
    }

    @Override
    public Object visit(ASTAdditiveExpression node, Object data) {
        if (node.getParent() instanceof ASTConditionalExpression || node.getNthParent(2) instanceof ASTConditionalExpression) {
            // ignore concats in ternary expressions
            return data;
        }

        ASTBlockStatement bs = node.getFirstParentOfType(ASTBlockStatement.class);
        if (bs == null) {
            return data;
        }

        int immediateLiterals = 0;
        int immediateStringLiterals = 0;
        List<ASTLiteral> nodes = node.findDescendantsOfType(ASTLiteral.class);
        for (ASTLiteral literal : nodes) {
            if (literal.getNthParent(3) instanceof ASTAdditiveExpression) {
                immediateLiterals++;
                if (literal.isStringLiteral()) {
                    immediateStringLiterals++;
                }
            }
            if (literal.isIntLiteral() || literal.isFloatLiteral() || literal.isDoubleLiteral()
                    || literal.isLongLiteral()) {
                return data;
            }
        }

        boolean onlyStringLiterals = immediateLiterals == immediateStringLiterals
                && immediateLiterals == node.getNumChildren();
        if (onlyStringLiterals) {
            return data;
        }

        // if literal + final, return
        List<ASTName> nameNodes = node.findDescendantsOfType(ASTName.class);
        for (ASTName name : nameNodes) {
            if (name.getNameDeclaration() != null && name.getNameDeclaration() instanceof VariableNameDeclaration) {
                VariableNameDeclaration vnd = (VariableNameDeclaration) name.getNameDeclaration();
                AccessNode accessNodeParent = vnd.getAccessNodeParent();
                if (accessNodeParent != null && accessNodeParent.isFinal()) {
                    return data;
                }
            }
        }

        // if literal primitive type and not strings variables, then return
        boolean stringFound = false;
        for (ASTName name : nameNodes) {
            if (!isPrimitiveType(name) && isStringType(name)) {
                stringFound = true;
                break;
            }
        }
        if (!stringFound && immediateStringLiterals == 0) {
            return data;
        }

        if (bs.isAllocation()) {
            for (Iterator<ASTName> iterator = nameNodes.iterator(); iterator.hasNext(); ) {
                ASTName name = iterator.next();
                if (!name.getImage().endsWith("length")) {
                    break;
                } else if (!iterator.hasNext()) {
                    return data; // All names end with length
                }
            }

            if (isAllocatedStringBuffer(node)) {
                addViolationWithPrecisePosition(data,node,INEFFICIENT_STRING_BUFFERING_VIOLATION_MSG);
            } else if (isInStringBufferOperationChain(node, "append")) {
                addViolationWithPrecisePosition(data,node,INEFFICIENT_STRING_BUFFERING_VIOLATION_MSG);
            }
        } else if (isInStringBufferOperationChain(node, "append")) {
            addViolationWithPrecisePosition(data,node,INEFFICIENT_STRING_BUFFERING_VIOLATION_MSG);
        }
        return data;
    }

    private boolean isStringType(ASTName name) {
        ASTType type = getTypeNode(name);
        if (type != null) {
            List<ASTClassOrInterfaceType> types = type.findDescendantsOfType(ASTClassOrInterfaceType.class);
            if (!types.isEmpty()) {
                return TypeTestUtil.isA(String.class, types.get(0));
            }
        }
        return false;
    }

    private boolean isPrimitiveType(ASTName name) {
        ASTType type = getTypeNode(name);
        return type != null && !type.findChildrenOfType(ASTPrimitiveType.class).isEmpty();
    }

    private ASTType getTypeNode(ASTName name) {
        ASTType result = null;
        if (name.getNameDeclaration() instanceof VariableNameDeclaration) {
            VariableNameDeclaration vnd = (VariableNameDeclaration) name.getNameDeclaration();
            if (vnd.getAccessNodeParent() instanceof ASTLocalVariableDeclaration) {
                ASTLocalVariableDeclaration l = (ASTLocalVariableDeclaration) vnd.getAccessNodeParent();
                result = l.getTypeNode();
            } else if (vnd.getAccessNodeParent() instanceof ASTFormalParameter) {
                ASTFormalParameter p = (ASTFormalParameter) vnd.getAccessNodeParent();
                result = p.getTypeNode();
            }
        }
        return result;
    }

    static boolean isInStringBufferOperationChain(Node node, String methodName) {
        ASTPrimaryExpression expr = node.getFirstParentOfType(ASTPrimaryExpression.class);
        FlightRuleUtils.MethodCallChain methodCalls = FlightRuleUtils.MethodCallChain.wrap(expr);
        while (expr != null && methodCalls == null) {
            expr = expr.getFirstParentOfType(ASTPrimaryExpression.class);
            methodCalls = FlightRuleUtils.MethodCallChain.wrap(expr);
        }

        if (methodCalls != null && !methodCalls.isExactlyOfAnyType(StringBuffer.class, StringBuilder.class)) {
            methodCalls = null;
        }

        return methodCalls != null && methodCalls.getMethodNames().contains(methodName);
    }

    /**
     * @deprecated will be removed with PMD 7
     */
    @Deprecated
    protected static boolean isInStringBufferOperation(Node node, int length, String methodName) {
        if (!(node.getNthParent(length) instanceof ASTStatementExpression)) {
            return false;
        }
        ASTStatementExpression s = node.getFirstParentOfType(ASTStatementExpression.class);
        if (s == null) {
            return false;
        }
        ASTName n = s.getFirstDescendantOfType(ASTName.class);
        if (n == null || !n.getImage().contains(methodName)
                || !(n.getNameDeclaration() instanceof VariableNameDeclaration)) {
            return false;
        }

        // TODO having to hand-code this kind of dredging around is ridiculous
        // we need something to support this in the framework
        // but, "for now" (tm):
        // if more than one arg to append(), skip it
        ASTArgumentList argList = s.getFirstDescendantOfType(ASTArgumentList.class);
        if (argList == null || argList.getNumChildren() > 1) {
            return false;
        }
        return FlightRuleUtils.isStringBuilderOrBuffer(((VariableNameDeclaration) n.getNameDeclaration()).getDeclaratorId());
    }

    private boolean isAllocatedStringBuffer(ASTAdditiveExpression node) {
        ASTAllocationExpression ao = node.getFirstParentOfType(ASTAllocationExpression.class);
        if (ao == null) {
            return false;
        }
        // note that the child can be an ArrayDimsAndInits, for example, from
        // java.lang.FloatingDecimal: t = new int[ nWords+wordcount+1 ];
        ASTClassOrInterfaceType an = ao.getFirstChildOfType(ASTClassOrInterfaceType.class);
        return FlightRuleUtils.isStringBuilderOrBuffer(an);
    }

    private static final String INEFFICIENT_STRING_BUFFERING_VIOLATION_MSG =
            "java.performance.InefficientStringBufferingRule.violation.msg";
}
