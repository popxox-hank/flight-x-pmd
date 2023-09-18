package com.ctrip.flight.mobile.pmd.lang.java.rule.codestyle;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightJavaRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.lang.java.types.TypeTestUtil;
import net.sourceforge.pmd.lang.symboltable.NameOccurrence;
import net.sourceforge.pmd.lang.symboltable.ScopedNode;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author haoren
 * Create at: 2023-07-21
 */
@Deprecated
public class UnnecessaryCastRule extends FlightJavaRule {

    private static Set<String> implClassNames = new HashSet<>();

    static {
        implClassNames.add("List");
        implClassNames.add("Set");
        implClassNames.add("Map");
        implClassNames.add("java.util.List");
        implClassNames.add("java.util.Set");
        implClassNames.add("java.util.Map");
        implClassNames.add("ArrayList");
        implClassNames.add("HashSet");
        implClassNames.add("HashMap");
        implClassNames.add("LinkedHashMap");
        implClassNames.add("LinkedHashSet");
        implClassNames.add("TreeSet");
        implClassNames.add("TreeMap");
        implClassNames.add("Vector");
        implClassNames.add("java.util.ArrayList");
        implClassNames.add("java.util.HashSet");
        implClassNames.add("java.util.HashMap");
        implClassNames.add("java.util.LinkedHashMap");
        implClassNames.add("java.util.LinkedHashSet");
        implClassNames.add("java.util.TreeSet");
        implClassNames.add("java.util.TreeMap");
        implClassNames.add("java.util.Vector");
        implClassNames.add("Iterator");
        implClassNames.add("java.util.Iterator");
    }

    @Override
    public Object visit(ASTLocalVariableDeclaration node, Object data) {
        process(node, data);
        return super.visit(node, data);
    }

    @Override
    public Object visit(ASTFieldDeclaration node, Object data) {
        process(node, data);
        return super.visit(node, data);
    }

    private void process(Node node, Object data) {
        ASTClassOrInterfaceType collectionType = node.getFirstDescendantOfType(ASTClassOrInterfaceType.class);
        if (collectionType == null || !implClassNames.contains(collectionType.getImage())) {
            return;
        }
        ASTClassOrInterfaceType cit = getCollectionItemType(collectionType);
        if (cit == null) {
            return;
        }
        ASTVariableDeclaratorId decl = node.getFirstDescendantOfType(ASTVariableDeclaratorId.class);
        List<NameOccurrence> usages = decl.getUsages();
        for (NameOccurrence no : usages) {
            ASTCastExpression castExpression = findCastExpression(no.getLocation());
            if (castExpression != null) {
                ASTClassOrInterfaceType castTarget =
                        castExpression.getFirstDescendantOfType(ASTClassOrInterfaceType.class);
                if (castTarget != null
                        && cit.getImage().equals(castTarget.getImage())
                        && !castTarget.hasDescendantOfType(ASTTypeArgument.class)) {
                    addViolationWithPrecisePosition(data, castExpression, UNNECESSARY_CAST_VIOLATION_MSG,
                            cit.getImage());
                }
            }
        }
    }

    private ASTClassOrInterfaceType getCollectionItemType(ASTClassOrInterfaceType collectionType) {
        if (TypeTestUtil.isA(Map.class, collectionType)) {
            List<ASTClassOrInterfaceType> types = collectionType.findDescendantsOfType(ASTClassOrInterfaceType.class);
            if (types.size() >= 2) {
                return types.get(1); // the value type of the map
            }
        } else {
            return collectionType.getFirstDescendantOfType(ASTClassOrInterfaceType.class);
        }
        return null;
    }

    private ASTCastExpression findCastExpression(ScopedNode usage) {
        Node n = usage.getNthParent(2);
        if (n instanceof ASTCastExpression) {
            return (ASTCastExpression) n;
        }
        n = n.getParent();
        if (n instanceof ASTCastExpression) {
            return (ASTCastExpression) n;
        }
        return null;
    }

    private static final String UNNECESSARY_CAST_VIOLATION_MSG =
            "java.code.style.UnnecessaryCastRule.violation.msg";
}
