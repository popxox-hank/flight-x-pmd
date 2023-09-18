package com.ctrip.flight.mobile.pmd.lang.java.rule.bestpractices;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightSunSecureRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.*;
import org.jaxen.JaxenException;

import java.util.List;

/**
 * @author haoren
 * Create at: 2023-07-19
 */
@Deprecated
public class MethodReturnsInternalArrayRule extends FlightSunSecureRule {

    public MethodReturnsInternalArrayRule() {
        addRuleChainVisit(ASTMethodDeclaration.class);
    }

    @Override
    public Object visit(ASTMethodDeclaration method, Object data) {
        if (!method.getResultType().returnsArray() || method.isPrivate()) {
            return data;
        }
        List<ASTReturnStatement> returns = method.findDescendantsOfType(ASTReturnStatement.class);
        ASTAnyTypeDeclaration td = method.getFirstParentOfType(ASTAnyTypeDeclaration.class);
        for (ASTReturnStatement ret : returns) {
            final String vn = getReturnedVariableName(ret);
            if (!isField(vn, td)) {
                continue;
            }
            if (ret.findDescendantsOfType(ASTPrimarySuffix.class).size() > 2) {
                continue;
            }
            if (ret.hasDescendantOfType(ASTAllocationExpression.class)) {
                continue;
            }
            if (hasArraysCopyOf(ret)) {
                continue;
            }
            if (hasClone(ret, vn)) {
                continue;
            }
            if (isEmptyArray(vn, td)) {
                continue;
            }
            if (!isLocalVariable(vn, method)) {
                addViolationWithPrecisePosition(data, ret, METHOD_RETURNS_INTERNAL_ARRAY_VIOLATION_MSG, vn);
            } else {
                // This is to handle field hiding
                final ASTPrimaryPrefix pp = ret.getFirstDescendantOfType(ASTPrimaryPrefix.class);
                if (pp != null && pp.usesThisModifier()) {
                    final ASTPrimarySuffix ps = ret.getFirstDescendantOfType(ASTPrimarySuffix.class);
                    if (ps.hasImageEqualTo(vn)) {
                        addViolationWithPrecisePosition(data, ret, METHOD_RETURNS_INTERNAL_ARRAY_VIOLATION_MSG, vn);
                    }
                }
            }
        }
        return data;
    }

    private boolean hasClone(ASTReturnStatement ret, String varName) {
        List<ASTPrimaryExpression> expressions = ret.findDescendantsOfType(ASTPrimaryExpression.class);
        for (ASTPrimaryExpression e : expressions) {
            if (e.getChild(0) instanceof ASTPrimaryPrefix && e.getNumChildren() == 2
                    && e.getChild(1) instanceof ASTPrimarySuffix
                    && ((ASTPrimarySuffix) e.getChild(1)).isArguments()
                    && ((ASTPrimarySuffix) e.getChild(1)).getArgumentCount() == 0) {
                ASTName name = e.getFirstDescendantOfType(ASTName.class);
                if (name != null && name.hasImageEqualTo(varName + ".clone")) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasArraysCopyOf(ASTReturnStatement ret) {
        List<ASTPrimaryExpression> expressions = ret.findDescendantsOfType(ASTPrimaryExpression.class);
        for (ASTPrimaryExpression e : expressions) {
            if (e.getNumChildren() == 2 && e.getChild(0) instanceof ASTPrimaryPrefix
                    && e.getChild(0).getNumChildren() == 1 && e.getChild(0).getChild(0) instanceof ASTName
                    && e.getChild(0).getChild(0).getImage().endsWith("Arrays.copyOf")) {
                return true;
            }
        }
        return false;
    }

    private boolean isEmptyArray(String varName, ASTAnyTypeDeclaration typeDeclaration) {
        final List<ASTFieldDeclaration> fds = typeDeclaration.findDescendantsOfType(ASTFieldDeclaration.class);
        if (fds != null) {
            for (ASTFieldDeclaration fd : fds) {
                final ASTVariableDeclaratorId vid = fd.getFirstDescendantOfType(ASTVariableDeclaratorId.class);
                if (fd.isFinal() && vid != null && vid.hasImageEqualTo(varName)) {
                    ASTVariableInitializer initializer = fd.getFirstDescendantOfType(ASTVariableInitializer.class);
                    if (initializer != null && initializer.getNumChildren() == 1) {
                        Node child = initializer.getChild(0);
                        if (child instanceof ASTArrayInitializer && child.getNumChildren() == 0) {
                            return true;
                        } else if (child instanceof ASTExpression) {
                            try {
                                List<? extends Node> arrayAllocation = child.findChildNodesWithXPath(
                                        "./PrimaryExpression/PrimaryPrefix/AllocationExpression/ArrayDimsAndInits" +
                                                "/Expression/PrimaryExpression/PrimaryPrefix/Literal[@IntLiteral" +
                                                "=\"true\"][@Image=\"0\"]");
                                if (arrayAllocation != null && arrayAllocation.size() == 1) {
                                    return true;
                                }
                            } catch (JaxenException e) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private static final String METHOD_RETURNS_INTERNAL_ARRAY_VIOLATION_MSG =
            "java.best.practices.MethodReturnsInternalArrayRule.violation.msg";
}
