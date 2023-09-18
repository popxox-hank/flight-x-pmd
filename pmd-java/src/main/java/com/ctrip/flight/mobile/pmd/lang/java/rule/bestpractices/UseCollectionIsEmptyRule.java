package com.ctrip.flight.mobile.pmd.lang.java.rule.bestpractices;

import com.ctrip.flight.mobile.pmd.lang.java.rule.AbstractFlightInefficientZeroCheck;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.lang.java.symboltable.ClassScope;
import net.sourceforge.pmd.lang.java.symboltable.JavaNameOccurrence;
import net.sourceforge.pmd.lang.java.symboltable.MethodNameDeclaration;
import net.sourceforge.pmd.lang.java.typeresolution.typedefinition.JavaTypeDefinition;
import net.sourceforge.pmd.lang.symboltable.NameOccurrence;
import net.sourceforge.pmd.util.CollectionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

/**
 * @author haoren
 * Create at: 2023-07-20
 */
@Deprecated
public class UseCollectionIsEmptyRule extends AbstractFlightInefficientZeroCheck {

    public UseCollectionIsEmptyRule() {
        addRuleChainVisit(ASTVariableDeclaratorId.class);
        addRuleChainVisit(ASTPrimarySuffix.class);
    }

    @Override
    public boolean appliesToClassName(String name) {
        return CollectionUtil.isCollectionType(name, true);
    }

    /**
     * Determine if we're dealing with .size method
     *
     * @param occ The name occurrence
     * @return true if it's .size, else false
     */
    @Override
    public boolean isTargetMethod(JavaNameOccurrence occ) {
        return occ.getNameForWhichThisIsAQualifier() != null
                && occ.getLocation().getImage().endsWith(".size");
    }

    @Override
    public Map<String, List<String>> getComparisonTargets() {
        List<String> zeroAndOne = asList("0", "1");
        List<String> zero = singletonList("0");
        Map<String, List<String>> rules = new HashMap<>();
        rules.put("<", zeroAndOne);
        rules.put(">", zero);
        rules.put("==", zero);
        rules.put("!=", zero);
        rules.put(">=", zeroAndOne);
        rules.put("<=", zero);
        return rules;
    }

    @Override
    public Object visit(ASTPrimarySuffix node, Object data) {
        if (isSizeMethodCall(node) && isCalledOnCollection(node)) {
            Node expr = node.getParent().getParent();
            checkNodeAndReport(data, node, expr);
        }
        return data;
    }

    @Override
    protected void checkNodeAndReport(Object data, Node location, Node expr) {
        if ((expr instanceof ASTEqualityExpression
                || expr instanceof ASTRelationalExpression && getComparisonTargets().containsKey(expr.getImage()))
                && isCompare(expr)) {
            addViolationWithPrecisePosition(data, location, USE_COLLECTION_IS_EMPTY_VIOLATION_MSG);
        }
    }

    private boolean isSizeMethodCall(ASTPrimarySuffix primarySuffix) {
        String calledMethodName = primarySuffix.getImage();
        return calledMethodName != null && "size" .equals(calledMethodName);
    }

    private boolean isCalledOnCollection(ASTPrimarySuffix primarySuffix) {
        JavaTypeDefinition calledOnType = getTypeOfVariable(primarySuffix);
        if (calledOnType == null) {
            calledOnType = getTypeOfMethodCall(primarySuffix);
        }
        return calledOnType != null
                && CollectionUtil.isCollectionType(calledOnType.getType(), true);
    }

    private JavaTypeDefinition getTypeOfVariable(ASTPrimarySuffix primarySuffix) {
        ASTPrimaryExpression primaryExpression = primarySuffix.getFirstParentOfType(ASTPrimaryExpression.class);
        ASTPrimaryPrefix varPrefix = primaryExpression.getFirstChildOfType(ASTPrimaryPrefix.class);
        if (prefixWithNoModifiers(varPrefix)) {
            return varPrefix.getTypeDefinition();
        }
        String varName = getVariableNameBySuffix(primaryExpression);
        return varName != null ? getTypeOfVariableByName(varName, primaryExpression) : null;
    }

    private boolean prefixWithNoModifiers(ASTPrimaryPrefix primaryPrefix) {
        return !primaryPrefix.usesSuperModifier() && !primaryPrefix.usesThisModifier();
    }

    private String getVariableNameBySuffix(ASTPrimaryExpression primaryExpression) {
        ASTPrimarySuffix varSuffix = primaryExpression
                .getFirstChildOfType(ASTPrimarySuffix.class);
        return varSuffix.getImage();
    }

    private JavaTypeDefinition getTypeOfVariableByName(String varName, ASTPrimaryExpression expr) {
        Node classOrEnumBody = expr.getFirstParentOfType(ASTClassOrInterfaceBody.class);
        if (classOrEnumBody == null) {
            classOrEnumBody = expr.getFirstParentOfType(ASTEnumBody.class);
        }
        if (classOrEnumBody == null) {
            classOrEnumBody = expr.getFirstParentOfType(ASTRecordDeclaration.class);
        }
        List<ASTVariableDeclaratorId> varDeclaratorIds =
                classOrEnumBody.findDescendantsOfType(ASTVariableDeclaratorId.class);
        for (ASTVariableDeclaratorId variableDeclaratorId : varDeclaratorIds) {
            if (variableDeclaratorId.getName().equals(varName)) {
                return variableDeclaratorId.getTypeNode().getTypeDefinition();
            }
        }
        return null;
    }

    private JavaTypeDefinition getTypeOfMethodCall(ASTPrimarySuffix node) {
        JavaTypeDefinition type = null;
        ASTName methodName = node.getParent().getFirstChildOfType(ASTPrimaryPrefix.class)
                .getFirstChildOfType(ASTName.class);
        if (methodName != null) {
            ClassScope classScope = node.getScope().getEnclosingScope(ClassScope.class);
            Map<MethodNameDeclaration, List<NameOccurrence>> methods = classScope.getMethodDeclarations();
            for (Map.Entry<MethodNameDeclaration, List<NameOccurrence>> e : methods.entrySet()) {
                if (e.getKey().getName().equals(methodName.getImage())) {
                    type = e.getKey().getNode().getFirstParentOfType(ASTMethodDeclaration.class)
                            .getFirstChildOfType(ASTResultType.class)
                            .getFirstDescendantOfType(ASTType.class)
                            .getTypeDefinition();
                    break;
                }
            }
        }
        return type;
    }

    private static final String USE_COLLECTION_IS_EMPTY_VIOLATION_MSG =
            "java.best.practices.UseCollectionIsEmptyRule.violation.msg";
}
