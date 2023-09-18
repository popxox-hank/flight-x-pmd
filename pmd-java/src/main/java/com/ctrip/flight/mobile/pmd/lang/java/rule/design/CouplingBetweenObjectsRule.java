package com.ctrip.flight.mobile.pmd.lang.java.rule.design;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightJavaRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.lang.java.symboltable.ClassScope;
import net.sourceforge.pmd.properties.PropertyDescriptor;
import net.sourceforge.pmd.properties.PropertyFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static net.sourceforge.pmd.properties.constraints.NumericConstraints.positive;

/**
 * @author haoren
 * Create at: 2023-07-24
 */
public class CouplingBetweenObjectsRule extends FlightJavaRule {

    private int couplingCount;
    private Set<String> typesFoundSoFar;

    private static final PropertyDescriptor<Integer> THRESHOLD_DESCRIPTOR
            = PropertyFactory.intProperty("threshold")
            .desc("Unique type reporting threshold")
            .require(positive()).defaultValue(20).build();

    public CouplingBetweenObjectsRule() {
        definePropertyDescriptor(THRESHOLD_DESCRIPTOR);
    }

    @Override
    public Object visit(ASTCompilationUnit cu, Object data) {
        typesFoundSoFar = new HashSet<>();
        couplingCount = 0;

        Object returnObj = super.visit(cu, data);

        int threshold = getProperty(THRESHOLD_DESCRIPTOR);
        if (couplingCount > threshold) {
            addViolationWithPrecisePosition(data, cu, COUPLING_BETWEEN_OBJECTS_VIOLATION_MSG, couplingCount, threshold);
        }

        return returnObj;
    }

    @Override
    public Object visit(ASTResultType node, Object data) {
        for (int x = 0; x < node.getNumChildren(); x++) {
            Node tNode = node.getChild(x);
            if (tNode instanceof ASTType) {
                Node reftypeNode = tNode.getChild(0);
                if (reftypeNode instanceof ASTReferenceType) {
                    Node classOrIntType = reftypeNode.getChild(0);
                    if (classOrIntType instanceof ASTClassOrInterfaceType) {
                        Node nameNode = classOrIntType;
                        this.checkVariableType(nameNode, nameNode.getImage());
                    }
                }
            }
        }
        return super.visit(node, data);
    }

    @Override
    public Object visit(ASTLocalVariableDeclaration node, Object data) {
        handleASTTypeChildren(node);
        return super.visit(node, data);
    }

    @Override
    public Object visit(ASTFormalParameter node, Object data) {
        handleASTTypeChildren(node);
        return super.visit(node, data);
    }

    @Override
    public Object visit(ASTFieldDeclaration node, Object data) {
        for (int x = 0; x < node.getNumChildren(); ++x) {
            Node firstStmt = node.getChild(x);
            if (firstStmt instanceof ASTType) {
                ASTType tp = (ASTType) firstStmt;
                Node nd = tp.getChild(0);
                checkVariableType(nd, nd.getImage());
            }
        }

        return super.visit(node, data);
    }

    /**
     * Convenience method to handle hierarchy. This is probably too much work and
     * will go away once I figure out the framework
     */
    private void handleASTTypeChildren(Node node) {
        for (int x = 0; x < node.getNumChildren(); x++) {
            Node sNode = node.getChild(x);
            if (sNode instanceof ASTType) {
                Node nameNode = sNode.getChild(0);
                checkVariableType(nameNode, nameNode.getImage());
            }
        }
    }

    /**
     * performs a check on the variable and updates the counter. Counter is
     * instance for a class and is reset upon new class scan.
     *
     * @param variableType The variable type.
     */
    private void checkVariableType(Node nameNode, String variableType) {
        List<ASTClassOrInterfaceDeclaration> parentTypes =
                nameNode.getParentsOfType(ASTClassOrInterfaceDeclaration.class);

        // TODO - move this into the symbol table somehow?
        if (parentTypes.isEmpty()) {
            return;
        }

        // skip interfaces
        if (parentTypes.get(0).isInterface()) {
            return;
        }

        // if the field is of any type other than the class type
        // increment the count
        ClassScope clzScope = ((JavaNode) nameNode).getScope().getEnclosingScope(ClassScope.class);
        if (!clzScope.getClassName().equals(variableType) && !this.filterTypes(variableType)
                && !this.typesFoundSoFar.contains(variableType)) {
            couplingCount++;
            typesFoundSoFar.add(variableType);
        }
    }

    /**
     * Filters variable type - we don't want primitives, wrappers, strings, etc.
     * This needs more work. I'd like to filter out super types and perhaps
     * interfaces
     *
     * @param variableType The variable type.
     * @return boolean true if variableType is not what we care about
     */
    private boolean filterTypes(String variableType) {
        return variableType != null && (variableType.startsWith("java.lang.") || "String" .equals(variableType)
                || filterPrimitivesAndWrappers(variableType));
    }

    /**
     * @param variableType The variable type.
     * @return boolean true if variableType is a primitive or wrapper
     */
    private boolean filterPrimitivesAndWrappers(String variableType) {
        return "int" .equals(variableType) || "Integer" .equals(variableType) || "char" .equals(variableType)
                || "Character" .equals(variableType) || "double" .equals(variableType) || "long" .equals(variableType)
                || "short" .equals(variableType) || "float" .equals(variableType) || "byte" .equals(variableType)
                || "boolean" .equals(variableType);
    }

    private static final String COUPLING_BETWEEN_OBJECTS_VIOLATION_MSG =
            "java.design.CouplingBetweenObjectsRule.violation.msg";
}
