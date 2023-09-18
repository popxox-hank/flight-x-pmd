package com.ctrip.flight.mobile.pmd.lang.java.rule.errorprone;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightJavaRule;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclarator;

import java.util.List;

/**
 * @author haoren
 * Create at: 2023-08-03
 */
public class MethodWithSameNameAsEnclosingClassRule extends FlightJavaRule {

    @Override
    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        List<ASTMethodDeclarator> methods = node.findDescendantsOfType(ASTMethodDeclarator.class);
        for (ASTMethodDeclarator m : methods) {
            if (m.hasImageEqualTo(node.getImage())) {
                addViolationWithPrecisePosition(data, m, METHOD_WITH_SAME_NAME_AS_ENCLOSING_CLASS_VIOLATION_MSG);
            }
        }
        return super.visit(node, data);
    }

    private static final String METHOD_WITH_SAME_NAME_AS_ENCLOSING_CLASS_VIOLATION_MSG =
            "java.errorprone.MethodWithSameNameAsEnclosingClassRule.violation.msg";
}
