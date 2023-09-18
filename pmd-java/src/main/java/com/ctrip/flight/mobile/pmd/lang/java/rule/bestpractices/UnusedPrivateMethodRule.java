package com.ctrip.flight.mobile.pmd.lang.java.rule.bestpractices;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightIgnoredAnnotationRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.lang.java.symboltable.ClassScope;
import net.sourceforge.pmd.lang.java.symboltable.MethodNameDeclaration;
import net.sourceforge.pmd.lang.java.types.TypeTestUtil;
import net.sourceforge.pmd.lang.symboltable.NameOccurrence;
import net.sourceforge.pmd.util.StringUtil;

import java.util.*;

/**
 * @author haoren
 * Create at: 2023-07-20
 */
public class UnusedPrivateMethodRule extends FlightIgnoredAnnotationRule {
    private static final Set<String> SERIALIZATION_METHODS = new HashSet<>(Arrays.asList(
            "readObject", "writeObject", "readResolve", "writeReplace"));

    public UnusedPrivateMethodRule() {
        addRuleChainVisit(ASTClassOrInterfaceDeclaration.class);
    }

    @Override
    protected Collection<String> defaultSuppressionAnnotations() {
        return Collections.singletonList("java.lang.Deprecated");
    }

    /**
     * Return a set of method names which are considered used. Only the
     * no-arg overload is considered used.
     */
    private static Set<String> methodsUsedByAnnotations(ASTClassOrInterfaceDeclaration klassDecl) {
        Set<String> result = Collections.emptySet();
        for (ASTAnyTypeBodyDeclaration declaration : klassDecl.getDeclarations()) {
            for (ASTAnnotation annot : declaration.findChildrenOfType(ASTAnnotation.class)) {
                if (TypeTestUtil.isA("org.junit.jupiter.params.provider.MethodSource", annot)) {
                    for (ASTLiteral literal : annot.findDescendantsOfType(ASTLiteral.class)) {
                        if (literal.isStringLiteral()) {
                            if (result.isEmpty()) {
                                result = new HashSet<>();
                            }
                            result.add(StringUtil.removeDoubleQuotes(literal.getImage()));
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        if (node.isInterface()) {
            return data;
        }

        Set<String> methodsUsedByAnnotations = methodsUsedByAnnotations(node);

        Map<MethodNameDeclaration, List<NameOccurrence>> methods = node.getScope().getEnclosingScope(ClassScope.class)
                .getMethodDeclarations();
        for (MethodNameDeclaration mnd : findUnique(methods)) {
            List<NameOccurrence> occs = methods.get(mnd);
            if (!privateAndNotExcluded(mnd)
                    || hasIgnoredAnnotation((Annotatable) mnd.getNode().getParent())
                    || mnd.getParameterCount() == 0 && methodsUsedByAnnotations.contains(mnd.getName())) {
                continue;
            }
            if (occs.isEmpty()) {
                addViolationWithPrecisePosition(data, mnd.getNode(), UN_USED_PRIVATE_METHOD_VIOLATION_MSG,
                        mnd.getImage() + mnd.getParameterDisplaySignature());
            } else {
                if (isMethodNotCalledFromOtherMethods(mnd, occs)) {
                    addViolationWithPrecisePosition(data, mnd.getNode(), UN_USED_PRIVATE_METHOD_VIOLATION_MSG,
                            mnd.getImage() + mnd.getParameterDisplaySignature());
                }

            }
        }
        return data;
    }

    private Set<MethodNameDeclaration> findUnique(Map<MethodNameDeclaration, List<NameOccurrence>> methods) {
        Set<MethodNameDeclaration> unique = new HashSet<>();
        Set<String> sigs = new HashSet<>();
        for (MethodNameDeclaration mnd : methods.keySet()) {
            String sig = mnd.getImage() + mnd.getParameterCount() + mnd.isVarargs();
            if (!sigs.contains(sig)) {
                unique.add(mnd);
            }
            sigs.add(sig);
        }
        return unique;
    }

    /**
     * Checks, whether the given method {@code mnd} is called from other methods or constructors.
     *
     * @param mnd  the private method, that is checked
     * @param occs the usages of the private method
     * @return <code>true</code> if the method is not used (except maybe from itself), <code>false</code>
     * if the method is called by other methods.
     */
    private boolean isMethodNotCalledFromOtherMethods(MethodNameDeclaration mnd, List<NameOccurrence> occs) {
        int callsFromOutsideMethod = 0;
        for (NameOccurrence occ : occs) {
            Node occNode = occ.getLocation();
            ASTConstructorDeclaration enclosingConstructor = occNode
                    .getFirstParentOfType(ASTConstructorDeclaration.class);
            if (enclosingConstructor != null) {
                callsFromOutsideMethod++;
                break; // Do we miss unused private constructors here?
            }
            ASTInitializer enclosingInitializer = occNode.getFirstParentOfType(ASTInitializer.class);
            if (enclosingInitializer != null) {
                callsFromOutsideMethod++;
                break;
            }

            ASTMethodDeclaration enclosingMethod = occNode.getFirstParentOfType(ASTMethodDeclaration.class);
            if (enclosingMethod == null || !mnd.getNode().getParent().equals(enclosingMethod)) {
                callsFromOutsideMethod++;
                break;
            }
        }
        return callsFromOutsideMethod == 0;
    }

    private boolean privateAndNotExcluded(MethodNameDeclaration mnd) {
        ASTMethodDeclaration node = mnd.getMethodNameDeclaratorNode().getParent();
        return node.isPrivate() && !SERIALIZATION_METHODS.contains(node.getName());
    }

    private static final String UN_USED_PRIVATE_METHOD_VIOLATION_MSG =
            "java.best.practices.UnusedPrivateMethodRule.violation.msg";
}
