package com.ctrip.flight.mobile.pmd.lang.java.rule.bestpractices;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightJavaRule;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.lang.java.symboltable.MethodScope;
import net.sourceforge.pmd.lang.java.symboltable.VariableNameDeclaration;
import net.sourceforge.pmd.lang.java.types.TypeTestUtil;
import net.sourceforge.pmd.lang.symboltable.NameDeclaration;
import net.sourceforge.pmd.lang.symboltable.NameOccurrence;
import net.sourceforge.pmd.properties.PropertyDescriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static net.sourceforge.pmd.properties.PropertyFactory.stringListProperty;

/**
 * @author haoren
 * Create at: 2023-07-20
 */
@Deprecated
public class UseTryWithResourcesRule extends FlightJavaRule {

    private static final PropertyDescriptor<List<String>> CLOSE_METHODS =
            stringListProperty("closeMethods")
                    .desc("Method names in finally block, which trigger this rule")
                    .defaultValues("close", "closeQuietly")
                    .delim(',')
                    .build();

    public UseTryWithResourcesRule() {
        addRuleChainVisit(ASTTryStatement.class);
        definePropertyDescriptor(CLOSE_METHODS);
    }

    @Override
    public Object visit(ASTTryStatement node, Object data) {
        boolean isJava9OrLater = isJava9OrLater((RuleContext) data);

        ASTFinallyStatement finallyClause = node.getFinallyClause();
        if (finallyClause != null) {
            List<ASTName> methods = findCloseMethods(finallyClause.findDescendantsOfType(ASTName.class));
            for (ASTName method : methods) {
                ASTName closeTarget = getCloseTarget(method);
                if (TypeTestUtil.isA(AutoCloseable.class, closeTarget)
                        && (isJava9OrLater || isLocalVar(closeTarget))) {
                    addViolationWithPrecisePosition(data, node, USE_TRY_WITH_RESOURCES_VIOLATION_MSG);
                    break;
                }
            }
        }
        return data;
    }

    private boolean isJava9OrLater(RuleContext ruleContext) {
        String currentVersion = ruleContext.getLanguageVersion().getVersion();
        currentVersion = currentVersion.replace("-preview", "");
        return Double.parseDouble(currentVersion) >= 9;
    }

    private boolean isLocalVar(ASTName closeTarget) {
        NameDeclaration nameDeclaration = closeTarget.getNameDeclaration();
        if (nameDeclaration instanceof VariableNameDeclaration) {
            ASTVariableDeclaratorId id = ((VariableNameDeclaration) nameDeclaration).getDeclaratorId();
            return id.isLocalVariable();
        } else if (closeTarget.getImage().contains(".")) {
            // this is a workaround for a bug in the symbol table:
            // the name might be resolved to a wrong method
            int lastDot = closeTarget.getImage().lastIndexOf('.');
            String varName = closeTarget.getImage().substring(0, lastDot);
            Map<VariableNameDeclaration, List<NameOccurrence>> vars = closeTarget.getScope()
                    .getEnclosingScope(MethodScope.class)
                    .getDeclarations(VariableNameDeclaration.class);
            for (VariableNameDeclaration varDecl : vars.keySet()) {
                if (varDecl.getName().equals(varName)) {
                    return varDecl.getDeclaratorId().isLocalVariable();
                }
            }
        }
        return false;
    }

    private ASTName getCloseTarget(ASTName method) {
        ASTArguments arguments = method.getNthParent(2).getFirstDescendantOfType(ASTArguments.class);
        if (arguments.size() > 0) {
            ASTName firstArgument = arguments.getChild(0).getChild(0).getFirstDescendantOfType(ASTName.class);
            if (firstArgument != null) {
                return firstArgument;
            }
        }

        return method;
    }

    private List<ASTName> findCloseMethods(List<ASTName> names) {
        List<ASTName> potentialCloses = new ArrayList<>();
        for (ASTName name : names) {
            String image = name.getImage();
            int lastDot = image.lastIndexOf('.');
            if (lastDot > -1) {
                image = image.substring(lastDot + 1);
            }
            if (getProperty(CLOSE_METHODS).contains(image) && isMethodCall(name)) {
                potentialCloses.add(name);
            }
        }
        return potentialCloses;
    }

    private boolean isMethodCall(ASTName potentialMethodCall) {
        return potentialMethodCall.getNthParent(2) instanceof ASTPrimaryExpression
                && !potentialMethodCall.getNthParent(2).findChildrenOfType(ASTPrimarySuffix.class).isEmpty();
    }

    private static final String USE_TRY_WITH_RESOURCES_VIOLATION_MSG =
            "java.best.practices.UseTryWithResourcesRule.violation.msg";
}
