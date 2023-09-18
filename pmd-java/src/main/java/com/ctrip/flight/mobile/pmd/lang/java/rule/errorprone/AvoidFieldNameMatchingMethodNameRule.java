package com.ctrip.flight.mobile.pmd.lang.java.rule.errorprone;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightJavaRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.*;

import java.util.*;

/**
 * @author haoren
 * Create at: 2023-08-03
 */
public class AvoidFieldNameMatchingMethodNameRule extends FlightJavaRule {

    @Override
    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        if (node.isInterface()) {
            return data;
        }
        return super.visit(node, data);
    }

    @Override
    public Object visit(ASTClassOrInterfaceBody node, Object data) {
        handleClassOrEnum(node, data);
        return super.visit(node, data);
    }

    @Override
    public Object visit(ASTEnumBody node, Object data) {
        handleClassOrEnum(node, data);
        return super.visit(node, data);
    }

    private void handleClassOrEnum(JavaNode node, Object data) {
        int n = node.getNumChildren();
        List<ASTFieldDeclaration> fields = new ArrayList<>();
        Set<String> methodNames = new HashSet<>();
        for (int i = 0; i < n; i++) {
            Node child = node.getChild(i);
            if (child.getNumChildren() == 0) {
                continue;
            }
            child = child.getChild(child.getNumChildren() - 1);
            if (child instanceof ASTFieldDeclaration) {
                fields.add((ASTFieldDeclaration) child);
            } else if (child instanceof ASTMethodDeclaration) {
                methodNames.add(((ASTMethodDeclaration) child).getName().toLowerCase(Locale.ROOT));
            }
        }
        for (ASTFieldDeclaration field : fields) {
            String varName = field.getVariableName().toLowerCase(Locale.ROOT);
            if (methodNames.contains(varName)) {
                addViolationWithPrecisePosition(data, field,
                        AVOID_FIELD_NAME_MATCHING_METHOD_NAME_VIOLATION_MSG, field.getVariableName());
            }
        }
    }

    private static final String AVOID_FIELD_NAME_MATCHING_METHOD_NAME_VIOLATION_MSG =
            "java.errorprone.AvoidFieldNameMatchingMethodNameRule.violation.msg";
}
