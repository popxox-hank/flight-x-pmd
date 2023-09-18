package com.ctrip.flight.mobile.pmd.lang.java.rule.codestyle;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightJavaRule;
import net.sourceforge.pmd.lang.java.ast.ASTForStatement;
import net.sourceforge.pmd.lang.java.ast.ASTLocalVariableDeclaration;
import net.sourceforge.pmd.lang.java.symboltable.VariableNameDeclaration;
import net.sourceforge.pmd.lang.symboltable.NameOccurrence;
import net.sourceforge.pmd.lang.symboltable.Scope;
import net.sourceforge.pmd.properties.PropertyDescriptor;

import java.util.List;
import java.util.Map;

import static net.sourceforge.pmd.properties.PropertyFactory.booleanProperty;

/**
 * @author haoren
 * Create at: 2023-07-21
 */
@Deprecated
public class LocalVariableCouldBeFinalRule extends FlightJavaRule {

    private static final PropertyDescriptor<Boolean> IGNORE_FOR_EACH =
            booleanProperty("ignoreForEachDecl").defaultValue(false).desc("Ignore non-final loop variables in a " +
                    "for-each statement.").build();

    public LocalVariableCouldBeFinalRule() {
        definePropertyDescriptor(IGNORE_FOR_EACH);
        addRuleChainVisit(ASTLocalVariableDeclaration.class);
    }

    @Override
    public Object visit(ASTLocalVariableDeclaration node, Object data) {
        if (node.isFinal()) {
            return data;
        }
        if (getProperty(IGNORE_FOR_EACH) && node.getParent() instanceof ASTForStatement) {
            return data;
        }
        Scope s = node.getScope();
        Map<VariableNameDeclaration, List<NameOccurrence>> decls = s.getDeclarations(VariableNameDeclaration.class);
        for (Map.Entry<VariableNameDeclaration, List<NameOccurrence>> entry : decls.entrySet()) {
            VariableNameDeclaration var = entry.getKey();
            if (var.getAccessNodeParent() != node) {
                continue;
            }
            if (!assigned(entry.getValue())) {
                addViolationWithPrecisePosition(data, var.getAccessNodeParent(),
                        LOCAL_VARIABLE_COULD_BE_FINAL_VIOLATION_MSG, var.getImage());
            }
        }
        return data;
    }

    private static final String LOCAL_VARIABLE_COULD_BE_FINAL_VIOLATION_MSG =
            "java.code.style.LocalVariableCouldBeFinalRule.violation.msg";
}
