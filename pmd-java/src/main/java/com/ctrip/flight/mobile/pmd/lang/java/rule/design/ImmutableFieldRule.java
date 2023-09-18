package com.ctrip.flight.mobile.pmd.lang.java.rule.design;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightLombokAwareRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.lang.java.symboltable.JavaNameOccurrence;
import net.sourceforge.pmd.lang.java.symboltable.VariableNameDeclaration;
import net.sourceforge.pmd.lang.symboltable.NameOccurrence;

import java.util.*;

/**
 * @author haoren
 * Create at: 2023-07-24
 */
public class ImmutableFieldRule extends FlightLombokAwareRule {


    @Override
    protected String defaultIgnoredAnnotationsDescription() {
        return "deprecated! " + super.defaultIgnoredAnnotationsDescription();
    }

    @Override
    protected Collection<String> defaultSuppressionAnnotations() {
        return Collections.emptyList();
    }

    @Override
    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        Object result = super.visit(node, data);

        Map<VariableNameDeclaration, List<NameOccurrence>> vars = node.getScope()
                .getDeclarations(VariableNameDeclaration.class);
        Set<ASTConstructorDeclaration> constructors =
                Collections.unmodifiableSet(new HashSet<>(findAllConstructors(node)));
        for (Map.Entry<VariableNameDeclaration, List<NameOccurrence>> entry : vars.entrySet()) {
            VariableNameDeclaration field = entry.getKey();
            AccessNode accessNodeParent = field.getAccessNodeParent();
            if (accessNodeParent.isStatic() || !accessNodeParent.isPrivate() || accessNodeParent.isFinal()
                    || accessNodeParent.isVolatile()
                    || hasLombokAnnotation(node)) {
                continue;
            }

            List<NameOccurrence> usages = entry.getValue();
            if (isImmutableField(field, usages, constructors)) {
                addViolationWithPrecisePosition(data, field.getNode(), IMMUTABLE_FIELD_VIOLATION_MSG, field.getImage());
            }
        }
        return result;
    }

    private boolean initializedWhenDeclared(VariableNameDeclaration field) {
        return field.getAccessNodeParent().hasDescendantOfType(ASTVariableInitializer.class);
    }

    private boolean isImmutableField(VariableNameDeclaration field, List<NameOccurrence> usages,
                                     Set<ASTConstructorDeclaration> allConstructors) {
        Set<ASTConstructorDeclaration> consSet = new HashSet<>(); // set of constructors accessing the field
        for (NameOccurrence occ : usages) {
            JavaNameOccurrence jocc = (JavaNameOccurrence) occ;
            if (jocc.isOnLeftHandSide() || jocc.isSelfAssignment()) {
                Node node = jocc.getLocation();
                ASTConstructorDeclaration constructor = node.getFirstParentOfType(ASTConstructorDeclaration.class);
                if (constructor != null && isSameClass(field, constructor)) {
                    if (inLoopOrTry(node)) {
                        return false;
                    }

                    if (inAnonymousInnerClass(node) || isInLambda(node)) {
                        return false; // leaks
                    } else {
                        consSet.add(constructor);
                    }
                } else {
                    // assigned outside of ctors.
                    return false;
                }
            }
        }

        return (allConstructors.equals(consSet) && !allConstructors.isEmpty())
                ^ initializedWhenDeclared(field);
    }

    private boolean isInLambda(Node node) {
        return node.getFirstParentOfType(ASTLambdaExpression.class) != null;
    }

    /**
     * Checks whether the given constructor belongs to the class, in which the field is declared.
     * This might not be the case for inner classes, which accesses the fields of the outer class.
     */
    private boolean isSameClass(VariableNameDeclaration field, ASTConstructorDeclaration constructor) {
        return constructor.getFirstParentOfType(ASTClassOrInterfaceBody.class) == field.getNode().getFirstParentOfType(ASTClassOrInterfaceBody.class);
    }

    private boolean inLoopOrTry(Node node) {
        return node.getFirstParentOfType(ASTTryStatement.class) != null
                || node.getFirstParentOfType(ASTForStatement.class) != null
                || node.getFirstParentOfType(ASTWhileStatement.class) != null
                || node.getFirstParentOfType(ASTDoStatement.class) != null;
    }

    private boolean inAnonymousInnerClass(Node node) {
        ASTClassOrInterfaceBodyDeclaration parent = node.getFirstParentOfType(ASTClassOrInterfaceBodyDeclaration.class);
        return parent != null && parent.isAnonymousInnerClass();
    }

    private List<ASTConstructorDeclaration> findAllConstructors(ASTClassOrInterfaceDeclaration node) {
        return node.getFirstChildOfType(ASTClassOrInterfaceBody.class)
                .findDescendantsOfType(ASTConstructorDeclaration.class);
    }

    private static final String IMMUTABLE_FIELD_VIOLATION_MSG =
            "java.design.ImmutableFieldRule.violation.msg";
}
