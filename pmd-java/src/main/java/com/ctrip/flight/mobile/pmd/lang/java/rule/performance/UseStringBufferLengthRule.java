package com.ctrip.flight.mobile.pmd.lang.java.rule.performance;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightJavaRule;
import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightRuleUtils;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.lang.java.symboltable.VariableNameDeclaration;
import net.sourceforge.pmd.lang.symboltable.NameDeclaration;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author haoren
 * Create at: 2023-07-26
 */
public class UseStringBufferLengthRule extends FlightJavaRule {

    private Set<NameDeclaration> alreadySeen = new HashSet<>();

    @Override
    public Object visit(ASTMethodDeclaration acu, Object data) {
        alreadySeen.clear();
        return super.visit(acu, data);
    }

    @Override
    public Object visit(ASTName decl, Object data) {
        if (!decl.getImage().endsWith("toString")) {
            return super.visit(decl, data);
        }
        NameDeclaration nd = decl.getNameDeclaration();
        if (nd == null) {
            return super.visit(decl, data);
        }
        if (alreadySeen.contains(nd) || !(nd instanceof VariableNameDeclaration)
                || !FlightRuleUtils.isStringBuilderOrBuffer(((VariableNameDeclaration) nd).getDeclaratorId())) {
            return super.visit(decl, data);
        }
        alreadySeen.add(nd);

        if (isViolation(decl)) {
            addViolationWithPrecisePosition(data, decl, USE_STRING_BUFFER_LENGTH_VIOLATION_MSG);
        }

        return super.visit(decl, data);
    }

    /**
     * Returns true for the following violations:
     *
     * <pre>
     * StringBuffer sb = new StringBuffer(&quot;some string&quot;);
     * if (sb.toString().equals(&quot;&quot;)) {
     *     // this is a violation
     * }
     * if (sb.toString().length() == 0) {
     *     // this is a violation
     * }
     * if (sb.length() == 0) {
     *     // this is ok
     * }
     * </pre>
     */
    private boolean isViolation(ASTName decl) {
        Node parent = decl.getParent().getParent();
        if (parent.getNumChildren() == 4) {
            if (parent.getChild(0).getFirstChildOfType(ASTName.class).getImage().endsWith(".toString")) {
                return isEqualsViolation(parent) || isLengthViolation(parent);
            }
        }
        return false;
    }

    private boolean isEqualsViolation(Node parent) {
        if (parent.getChild(2).hasImageEqualTo("equals")) {
            Node primarySuffix = parent.getChild(3);
            List<ASTArgumentList> methodCalls = primarySuffix.findDescendantsOfType(ASTArgumentList.class);
            if (methodCalls.size() == 1 && methodCalls.get(0).size() == 1) {
                ASTExpression firstArgument = primarySuffix.getChild(0).getFirstChildOfType(ASTArgumentList.class)
                        .findChildrenOfType(ASTExpression.class).get(0);
                List<ASTLiteral> literals = firstArgument.findDescendantsOfType(ASTLiteral.class);
                if (literals.isEmpty()) {
                    literals = findLiteralsInVariableInitializer(firstArgument);
                }
                return literals.size() == 1 && literals.get(0).hasImageEqualTo("\"\"");
            }
        }
        return false;
    }

    private List<ASTLiteral> findLiteralsInVariableInitializer(ASTExpression firstArgument) {
        List<ASTName> varAccess = firstArgument.findDescendantsOfType(ASTName.class);
        if (varAccess.size() == 1) {
            NameDeclaration nameDeclaration = varAccess.get(0).getNameDeclaration();
            if (nameDeclaration != null && nameDeclaration.getNode() instanceof ASTVariableDeclaratorId) {
                ASTVariableDeclaratorId varId = (ASTVariableDeclaratorId) nameDeclaration.getNode();
                if (varId.isFinal() && varId.getParent() instanceof ASTVariableDeclarator) {
                    ASTVariableDeclarator declarator = (ASTVariableDeclarator) varId.getParent();
                    if (declarator.getInitializer() != null) {
                        return declarator.getInitializer().findDescendantsOfType(ASTLiteral.class);
                    }
                }
            }
        }
        return Collections.emptyList();
    }

    private boolean isLengthViolation(Node parent) {
        return parent.getChild(2).hasImageEqualTo("length");
    }

    private static final String USE_STRING_BUFFER_LENGTH_VIOLATION_MSG =
            "java.performance.UseStringBufferLengthRule.violation.msg";
}
