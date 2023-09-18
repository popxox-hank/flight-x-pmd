package com.ctrip.flight.mobile.pmd.lang.java.rule.errorprone;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightJavaRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTConstructorDeclaration;
import net.sourceforge.pmd.lang.java.ast.AccessNode;
import net.sourceforge.pmd.lang.java.symboltable.JavaNameOccurrence;
import net.sourceforge.pmd.lang.java.symboltable.VariableNameDeclaration;
import net.sourceforge.pmd.lang.symboltable.NameOccurrence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author haoren
 * Create at: 2023-08-02
 */
public class AssignmentToNonFinalStaticRule extends FlightJavaRule {

    @Override
    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        Map<VariableNameDeclaration, List<NameOccurrence>> vars = node.getScope()
                .getDeclarations(VariableNameDeclaration.class);
        for (Map.Entry<VariableNameDeclaration, List<NameOccurrence>> entry : vars.entrySet()) {
            VariableNameDeclaration decl = entry.getKey();
            AccessNode accessNodeParent = decl.getAccessNodeParent();
            if (!accessNodeParent.isStatic() || accessNodeParent.isFinal()) {
                continue;
            }

            final List<Node> locations = initializedInConstructor(entry.getValue());
            for (final Node location : locations) {
                addViolationWithPrecisePosition(data, location, ASSIGNMENT_TO_NON_FINAL_STATIC_VIOLATION_MSG,
                        decl.getImage());
            }
        }
        return super.visit(node, data);
    }

    private List<Node> initializedInConstructor(List<NameOccurrence> usages) {
        final List<Node> unsafeAssignments = new ArrayList<>();
        for (NameOccurrence occ : usages) {
            if (((JavaNameOccurrence) occ).isOnLeftHandSide()) {
                Node node = occ.getLocation();
                Node constructor = node.getFirstParentOfType(ASTConstructorDeclaration.class);
                if (constructor != null) {
                    unsafeAssignments.add(node);
                }
            }
        }

        return unsafeAssignments;
    }

    private static final String ASSIGNMENT_TO_NON_FINAL_STATIC_VIOLATION_MSG =
            "java.errorprone.AssignmentToNonFinalStaticRule.violation.msg";
}
