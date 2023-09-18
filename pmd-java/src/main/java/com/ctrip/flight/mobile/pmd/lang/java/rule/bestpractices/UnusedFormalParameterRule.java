package com.ctrip.flight.mobile.pmd.lang.java.rule.bestpractices;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightJavaRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.lang.java.rule.internal.JavaRuleUtil;
import net.sourceforge.pmd.lang.java.symboltable.JavaNameOccurrence;
import net.sourceforge.pmd.lang.java.symboltable.VariableNameDeclaration;
import net.sourceforge.pmd.lang.symboltable.NameOccurrence;
import net.sourceforge.pmd.properties.PropertyDescriptor;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Map;

import static net.sourceforge.pmd.properties.PropertyFactory.booleanProperty;

/**
 * @author haoren
 * Create at: 2023-07-19
 */
public class UnusedFormalParameterRule extends FlightJavaRule {

    private static final PropertyDescriptor<Boolean> CHECKALL_DESCRIPTOR =
            booleanProperty("checkAll")
                    .desc("Check all methods, including non-private ones")
                    .defaultValue(false)
                    .build();

    public UnusedFormalParameterRule() {
        definePropertyDescriptor(CHECKALL_DESCRIPTOR);
    }

    @Override
    public Object visit(ASTConstructorDeclaration node, Object data) {
        check(node, data);
        return super.visit(node, data);
    }

    @Override
    public Object visit(ASTMethodDeclaration node, Object data) {
        if (!node.isPrivate() && !getProperty(CHECKALL_DESCRIPTOR)) {
            return super.visit(node, data);
        }
        if (!node.isNative() && !node.isAbstract() && !isSerializationMethod(node) && !hasOverrideAnnotation(node)) {
            check(node, data);
        }
        return super.visit(node, data);
    }

    private boolean isSerializationMethod(ASTMethodDeclaration node) {
        ASTMethodDeclarator declarator = node.getFirstDescendantOfType(ASTMethodDeclarator.class);
        List<ASTFormalParameter> parameters = declarator.findDescendantsOfType(ASTFormalParameter.class);
        if (node.isPrivate() && "readObject" .equals(node.getName()) && parameters.size() == 1
                && throwsOneException(node, InvalidObjectException.class)) {
            ASTType type = parameters.get(0).getTypeNode();
            if (type.getType() == ObjectInputStream.class
                    || ObjectInputStream.class.getSimpleName().equals(type.getTypeImage())
                    || ObjectInputStream.class.getName().equals(type.getTypeImage())) {
                return true;
            }
        }
        return false;
    }

    private boolean throwsOneException(ASTMethodDeclaration node, Class<? extends Throwable> exception) {
        ASTNameList throwsList = node.getThrows();
        if (throwsList != null && throwsList.getNumChildren() == 1) {
            ASTName n = (ASTName) throwsList.getChild(0);
            if (n.getType() == exception || exception.getSimpleName().equals(n.getImage())
                    || exception.getName().equals(n.getImage())) {
                return true;
            }
        }
        return false;
    }

    private void check(Node node, Object data) {
        Node parent = node.getParent().getParent().getParent();
        if (parent instanceof ASTClassOrInterfaceDeclaration
                && !((ASTClassOrInterfaceDeclaration) parent).isInterface()
                || parent instanceof ASTAllocationExpression) {
            Map<VariableNameDeclaration, List<NameOccurrence>> vars = ((JavaNode) node).getScope()
                    .getDeclarations(VariableNameDeclaration.class);
            for (Map.Entry<VariableNameDeclaration, List<NameOccurrence>> entry : vars.entrySet()) {
                VariableNameDeclaration nameDecl = entry.getKey();

                ASTVariableDeclaratorId declNode = nameDecl.getDeclaratorId();
                if (!declNode.isFormalParameter() || declNode.isExplicitReceiverParameter()) {
                    continue;
                }

                if (actuallyUsed(nameDecl, entry.getValue())
                        || JavaRuleUtil.isExplicitUnusedVarName(nameDecl.getName())) {
                    continue;
                }
                if (node instanceof ASTMethodDeclaration) {
                    addViolationWithPrecisePosition(data, nameDecl.getNode(),
                            UN_USED_FORMAL_PARAMETER_METHOD_VIOLATION_MSG, nameDecl.getImage());
                } else {
                    addViolationWithPrecisePosition(data, nameDecl.getNode(),
                            UN_USED_FORMAL_PARAMETER_CONSTRUCTOR_VIOLATION_MSG, nameDecl.getImage());
                }
            }
        }
    }

    private boolean actuallyUsed(VariableNameDeclaration nameDecl, List<NameOccurrence> usages) {
        for (NameOccurrence occ : usages) {
            JavaNameOccurrence jocc = (JavaNameOccurrence) occ;
            if (jocc.isOnLeftHandSide()) {
                if (nameDecl.isArray() && jocc.getLocation().getParent().getParent().getNumChildren() > 1) {
                    // array element access
                    return true;
                }
                continue;
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean hasOverrideAnnotation(ASTMethodDeclaration node) {
        int childIndex = node.getIndexInParent();
        for (int i = 0; i < childIndex; i++) {
            Node previousSibling = node.getParent().getChild(i);
            List<ASTMarkerAnnotation> annotations = previousSibling.findDescendantsOfType(ASTMarkerAnnotation.class);
            for (ASTMarkerAnnotation annotation : annotations) {
                ASTName name = annotation.getFirstChildOfType(ASTName.class);
                if (name != null && (name.hasImageEqualTo("Override") || name.hasImageEqualTo("java.lang.Override"))) {
                    return true;
                }
            }
        }
        return false;
    }

    private static final String UN_USED_FORMAL_PARAMETER_METHOD_VIOLATION_MSG =
            "java.best.practices.UnusedFormalParameterRule.Method.violation.msg";
    private static final String UN_USED_FORMAL_PARAMETER_CONSTRUCTOR_VIOLATION_MSG =
            "java.best.practices.UnusedFormalParameterRule.Constructor.violation.msg";
}
