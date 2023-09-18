package com.ctrip.flight.mobile.pmd.lang.java.rule.performance;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightJavaRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTAdditiveExpression;
import net.sourceforge.pmd.lang.java.ast.ASTLiteral;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryExpression;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.lang.java.symboltable.JavaNameOccurrence;
import net.sourceforge.pmd.lang.symboltable.NameOccurrence;

import java.util.List;

/**
 * @author haoren
 * Create at: 2023-07-26
 */
public class UseIndexOfCharRule extends FlightJavaRule {

    private static final String TARGET_TYPE_NAME = "String";
    private static final String[] METHOD_NAMES = new String[]{"indexOf", "lastIndexOf"};

    private boolean isViolationArgument(Node arg) {
        return ((ASTLiteral) arg).isSingleCharacterStringLiteral();
    }

    /**
     * Returns whether the name occurrence is one of the method calls we are
     * interested in.
     *
     * @param occurrence NameOccurrence
     * @return boolean
     */
    private boolean isNotedMethod(NameOccurrence occurrence) {

        if (occurrence == null) {
            return false;
        }

        String methodCall = occurrence.getImage();

        for (String element : METHOD_NAMES) {
            if (methodCall.contains(element)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method visit.
     *
     * @param node ASTVariableDeclaratorId
     * @param data Object
     * @return Object
     * @see net.sourceforge.pmd.lang.java.ast.JavaParserVisitor#visit(ASTVariableDeclaratorId,
     * Object)
     */
    @Override
    public Object visit(ASTVariableDeclaratorId node, Object data) {
        if (node.getNameDeclaration() == null || !TARGET_TYPE_NAME.equals(node.getNameDeclaration().getTypeImage())) {
            return data;
        }

        for (NameOccurrence occ : node.getUsages()) {
            JavaNameOccurrence jocc = (JavaNameOccurrence) occ;
            if (isNotedMethod(jocc.getNameForWhichThisIsAQualifier())) {
                Node parent = jocc.getLocation().getParent().getParent();
                if (parent instanceof ASTPrimaryExpression) {
                    // bail out if it's something like indexOf("a" + "b")
                    if (parent.hasDescendantOfType(ASTAdditiveExpression.class)) {
                        return data;
                    }
                    List<ASTLiteral> literals = parent.findDescendantsOfType(ASTLiteral.class);
                    for (int l = 0; l < literals.size(); l++) {
                        ASTLiteral literal = literals.get(l);
                        if (isViolationArgument(literal)) {
                            addViolationWithPrecisePosition(data, jocc.getLocation(), USE_INDEX_OF_CHAR_VIOLATION_MSG);
                        }
                    }
                }
            }
        }
        return data;
    }

    private static final String USE_INDEX_OF_CHAR_VIOLATION_MSG =
            "java.performance.UseIndexOfCharRule.violation.msg";
}
