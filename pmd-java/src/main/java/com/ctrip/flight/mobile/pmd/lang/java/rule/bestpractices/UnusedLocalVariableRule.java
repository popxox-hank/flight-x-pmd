package com.ctrip.flight.mobile.pmd.lang.java.rule.bestpractices;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightJavaRule;
import net.sourceforge.pmd.lang.java.ast.ASTLocalVariableDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclarator;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.lang.java.rule.internal.JavaRuleUtil;
import net.sourceforge.pmd.lang.java.symboltable.JavaNameOccurrence;
import net.sourceforge.pmd.lang.symboltable.NameOccurrence;

import java.util.List;

/**
 * @author haoren
 * Create at: 2023-07-20
 */
public class UnusedLocalVariableRule extends FlightJavaRule {

    public UnusedLocalVariableRule() {
        addRuleChainVisit(ASTLocalVariableDeclaration.class);
    }

    @Override
    public Object visit(ASTLocalVariableDeclaration decl, Object data) {
        for (int i = 0; i < decl.getNumChildren(); i++) {
            if (!(decl.getChild(i) instanceof ASTVariableDeclarator)) {
                continue;
            }
            ASTVariableDeclaratorId node = (ASTVariableDeclaratorId) decl.getChild(i).getChild(0);
            if (!node.getNameDeclaration().isArray()
                    && !actuallyUsed(node.getUsages())
                    && !JavaRuleUtil.isExplicitUnusedVarName(node.getName())) {
                addViolationWithPrecisePosition(data, node, UN_USED_LOCAL_VARIABLE_VIOLATION_MSG,
                        node.getNameDeclaration().getImage());
            }
        }
        return data;
    }

    private boolean actuallyUsed(List<NameOccurrence> usages) {
        for (NameOccurrence occ : usages) {
            JavaNameOccurrence jocc = (JavaNameOccurrence) occ;
            if (!jocc.isOnLeftHandSide()) {
                return true;
            }
        }
        return false;
    }

    private static final String UN_USED_LOCAL_VARIABLE_VIOLATION_MSG =
            "java.best.practices.UnusedLocalVariableRule.violation.msg";
}
