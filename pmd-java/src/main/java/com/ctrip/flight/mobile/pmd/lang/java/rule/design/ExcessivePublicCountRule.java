package com.ctrip.flight.mobile.pmd.lang.java.rule.design;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightStatisticalJavaRule;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.stat.DataPoint;
import net.sourceforge.pmd.util.NumericConstants;

/**
 * @author haoren
 * Create at: 2023-07-31
 */
public class ExcessivePublicCountRule extends FlightStatisticalJavaRule {
    public ExcessivePublicCountRule() {
        setProperty(MINIMUM_DESCRIPTOR, 45d);
    }

    @Override
    public Object visit(JavaNode node, Object data) {
        int numNodes = 0;

        for (int i = 0; i < node.getNumChildren(); i++) {
            Integer treeSize = (Integer) ((JavaNode) node.getChild(i)).jjtAccept(this, data);
            numNodes += treeSize;
        }

        if (ASTCompilationUnit.class.isInstance(node)) {
            DataPoint point = new DataPoint();
            point.setNode(node);
            point.setScore(1.0 * numNodes);
            point.setMessage(EXCESSIVE_PUBLIC_LIST_VIOLATION_MSG);
            addDataPoint(point);
        }

        return Integer.valueOf(numNodes);
    }

    /**
     * Method counts ONLY public methods.
     */
    @Override
    public Object visit(ASTMethodDeclarator node, Object data) {
        return this.getTallyOnAccessType((AccessNode) node.getParent());
    }

    /**
     * Method counts ONLY public class attributes which are not PUBLIC and
     * static- these usually represent constants....
     */
    @Override
    public Object visit(ASTFieldDeclaration node, Object data) {
        if (node.isFinal() && node.isStatic()) {
            return NumericConstants.ZERO;
        }
        return this.getTallyOnAccessType(node);
    }

    /**
     * Method counts a node if it is public
     *
     * @param node The access node.
     * @return Integer 1 if node is public 0 otherwise
     */
    private Integer getTallyOnAccessType(AccessNode node) {
        if (node.isPublic()) {
            return NumericConstants.ONE;
        }
        return NumericConstants.ZERO;
    }

    private static final String EXCESSIVE_PUBLIC_LIST_VIOLATION_MSG =
            "java.design.ExcessivePublicCountRule.violation.msg";
}
