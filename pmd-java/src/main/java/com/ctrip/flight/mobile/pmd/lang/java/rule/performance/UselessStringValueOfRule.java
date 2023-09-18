package com.ctrip.flight.mobile.pmd.lang.java.rule.performance;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightJavaRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.lang.java.symboltable.MethodNameDeclaration;
import net.sourceforge.pmd.lang.java.symboltable.VariableNameDeclaration;
import net.sourceforge.pmd.lang.symboltable.NameDeclaration;

/**
 * @author haoren
 * Create at: 2023-07-26
 */
public class UselessStringValueOfRule extends FlightJavaRule {

    @Override
    public Object visit(ASTPrimaryPrefix node, Object data) {
        if (node.getNumChildren() == 0 || !(node.getChild(0) instanceof ASTName)) {
            return super.visit(node, data);
        }

        String image = ((ASTName) node.getChild(0)).getImage();

        if ("String.valueOf".equals(image)) {
            Node parent = node.getParent();
            if (parent.getNumChildren() != 2) {
                return super.visit(node, data);
            }
            ASTArgumentList args = parent.getFirstDescendantOfType(ASTArgumentList.class);
            if (args != null) {
                if (args.size() > 1) {
                    // skip String.valueOf with more than one argument (e.g. String.valueOf(char[],int,int))
                    return super.visit(node, data);
                }
                // skip String.valueOf(anyarraytype[])
                ASTName arg = args.getFirstDescendantOfType(ASTName.class);
                if (arg != null) {
                    NameDeclaration declaration = arg.getNameDeclaration();
                    if (declaration != null) {
                        ASTType argType = declaration.getNode().getParent().getParent()
                                .getFirstDescendantOfType(ASTType.class);
                        if (argType != null && argType.getChild(0) instanceof ASTReferenceType
                                && ((ASTReferenceType) argType.getChild(0)).isArray()) {
                            return super.visit(node, data);
                        }
                    }
                }
            }

            Node gp = parent.getParent();
            if (parent instanceof ASTPrimaryExpression && gp instanceof ASTAdditiveExpression
                    && "+".equals(gp.getImage())) {
                boolean ok = false;
                if (gp.getChild(0) == parent) {
                    ok = !isPrimitive(gp.getChild(1));
                } else {
                    for (int i = 0; !ok && gp.getChild(i) != parent; i++) {
                        ok = !isPrimitive(gp.getChild(i));
                    }
                }
                if (ok) {
                    addViolationWithPrecisePosition(data, node, USE_LESS_STRING_VALUE_OF_VIOLATION_MSG);
                    return data;
                }
            }
        }
        return super.visit(node, data);
    }

    private static boolean isPrimitive(Node parent) {
        boolean result = false;
        if (parent instanceof ASTPrimaryExpression && parent.getNumChildren() > 0) {
            Node child = parent.getChild(0);
            if (child instanceof ASTPrimaryPrefix && child.getNumChildren() == 1) {
                Node gc = child.getChild(0);
                if (gc instanceof ASTName) {
                    ASTName name = (ASTName) gc;
                    NameDeclaration nd = name.getNameDeclaration();
                    if (nd instanceof VariableNameDeclaration && ((VariableNameDeclaration) nd).isPrimitiveType()) {
                        result = true;
                    } else if (nd instanceof MethodNameDeclaration
                            && ((MethodNameDeclaration) nd).isPrimitiveReturnType()) {
                        result = true;
                    }
                } else if (gc instanceof ASTLiteral) {
                    result = !((ASTLiteral) gc).isStringLiteral();
                }
            }
        }
        return result;
    }

    private static final String USE_LESS_STRING_VALUE_OF_VIOLATION_MSG =
            "java.performance.UselessStringValueOfRule.violation.msg";
}
