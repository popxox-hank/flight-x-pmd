package com.ctrip.flight.mobile.pmd.lang.java.rule.bestpractices;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightJavaRule;
import net.sourceforge.pmd.lang.java.ast.*;


/**
 * @author haoren
 * Create at: 2023-07-17
 */
@Deprecated
public class AbstractClassWithoutAbstractMethodRule extends FlightJavaRule {

    public AbstractClassWithoutAbstractMethodRule() {
        addRuleChainVisit(ASTClassOrInterfaceDeclaration.class);
    }

    @Override
    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        if (!node.isAbstract() || doesExtend(node) || doesImplement(node)) {
            return data;
        }

        int countOfAbstractMethods = 0;
        for (ASTAnyTypeBodyDeclaration decl : node.getDeclarations()) {
            if (decl.getKind() == ASTAnyTypeBodyDeclaration.DeclarationKind.METHOD) {
                ASTMethodDeclaration methodDecl = (ASTMethodDeclaration) decl.getDeclarationNode();
                if (methodDecl.isAbstract()) {
                    countOfAbstractMethods++;
                }
            }
        }
        if (countOfAbstractMethods == 0) {
            addViolationWithPrecisePosition(data, node,
                    ABSTRACT_CLASS_WITHOUT_ABSTRACT_METHOD_VIOLATION_MSG, node.getImage());
        }
        return data;
    }

    private boolean doesExtend(ASTClassOrInterfaceDeclaration node) {
        return node.getFirstChildOfType(ASTExtendsList.class) != null;
    }

    private boolean doesImplement(ASTClassOrInterfaceDeclaration node) {
        return node.getFirstChildOfType(ASTImplementsList.class) != null;
    }

    private static final String ABSTRACT_CLASS_WITHOUT_ABSTRACT_METHOD_VIOLATION_MSG =
            "java.best.practices.AbstractClassWithoutAbstractMethodRule.violation.msg";
}
