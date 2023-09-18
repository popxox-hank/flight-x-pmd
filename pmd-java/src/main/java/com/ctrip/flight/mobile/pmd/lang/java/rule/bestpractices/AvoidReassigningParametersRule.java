package com.ctrip.flight.mobile.pmd.lang.java.rule.bestpractices;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightJavaRule;
import net.sourceforge.pmd.lang.java.ast.ASTConstructorDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclarator;
import net.sourceforge.pmd.lang.java.symboltable.JavaNameOccurrence;
import net.sourceforge.pmd.lang.java.symboltable.VariableNameDeclaration;
import net.sourceforge.pmd.lang.symboltable.NameOccurrence;

import java.util.List;
import java.util.Map;

/**
 * @author haoren
 * Create at: 2023-07-18
 */
public class AvoidReassigningParametersRule extends FlightJavaRule {

    @Override
    public Object visit(ASTMethodDeclarator node, Object data) {
        Map<VariableNameDeclaration, List<NameOccurrence>> params = node.getScope()
                .getDeclarations(VariableNameDeclaration.class);
        this.lookForViolation(params, data);
        return super.visit(node, data);
    }

    private void lookForViolation(Map<VariableNameDeclaration, List<NameOccurrence>> params, Object data) {
        for (Map.Entry<VariableNameDeclaration, List<NameOccurrence>> entry : params.entrySet()) {
            VariableNameDeclaration decl = entry.getKey();
            List<NameOccurrence> usages = entry.getValue();

            // Only look for formal parameters
            if (!decl.getDeclaratorId().isFormalParameter()) {
                continue;
            }

            for (NameOccurrence occ : usages) {
                JavaNameOccurrence jocc = (JavaNameOccurrence) occ;
                if ((jocc.isOnLeftHandSide() || jocc.isSelfAssignment())
                        && jocc.getNameForWhichThisIsAQualifier() == null && !jocc.useThisOrSuper() && !decl.isVarargs()
                        && (!decl.isArray()
                        || jocc.getLocation().getParent().getParent().getNumChildren() == 1)) {
                    // not an array or no primary suffix to access the array values
                    addViolationWithPrecisePosition(data, occ.getLocation(), AVOID_REASSIGNING_PARAMETERS_VIOLATION_MSG
                            , decl.getImage());
                    // only the first assignment should be reported
                    break;
                }
            }
        }
    }

    @Override
    public Object visit(ASTConstructorDeclaration node, Object data) {
        Map<VariableNameDeclaration, List<NameOccurrence>> params = node.getScope()
                .getDeclarations(VariableNameDeclaration.class);
        this.lookForViolation(params, data);
        return super.visit(node, data);
    }

    private static final String AVOID_REASSIGNING_PARAMETERS_VIOLATION_MSG =
            "java.best.practices.AvoidReassigningParametersRule.violation.msg";

}
