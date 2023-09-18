package com.ctrip.flight.mobile.pmd.lang.java.rule.design;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightStatisticalJavaRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTConstructorDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTFormalParameter;
import net.sourceforge.pmd.lang.java.ast.ASTFormalParameters;
import net.sourceforge.pmd.lang.java.ast.JavaNode;
import net.sourceforge.pmd.stat.DataPoint;

/**
 * @author haoren
 * Create at: 2023-07-31
 */
public class ExcessiveParameterListRule extends FlightStatisticalJavaRule {

    private static final Integer COUNT = 1;
    private static final Integer SKIP = 0;

    public ExcessiveParameterListRule() {
        setProperty(MINIMUM_DESCRIPTOR, 10d);
    }

    @Override
    public Object visit(ASTFormalParameters params, Object data) {
        if (areParametersOfPrivateConstructor(params)) {
            return SKIP;
        }
        return super.visit(params, data);
    }


    @Override
    public Object visit(JavaNode node, Object data) {
        int numNodes = 0;

        for (int i = 0; i < node.getNumChildren(); i++) {
            Integer treeSize = (Integer) ((JavaNode) node.getChild(i)).jjtAccept(this, data);
            numNodes += treeSize;
        }

        if (ASTFormalParameters.class.isInstance(node)) {
            DataPoint point = new DataPoint();
            point.setNode(node);
            point.setScore(1.0 * numNodes);
            point.setMessage(EXCESSIVE_PARAMETER_LIST_VIOLATION_MSG);
            addDataPoint(point);
        }

        return Integer.valueOf(numNodes);
    }

    private boolean areParametersOfPrivateConstructor(ASTFormalParameters params) {
        Node parent = params.getParent();
        if (parent instanceof ASTConstructorDeclaration) {
            ASTConstructorDeclaration constructor = (ASTConstructorDeclaration) parent;
            return constructor.isPrivate();
        }
        return false;
    }

    @Override
    public Object visit(ASTFormalParameter param, Object data) {
        return COUNT;
    }

    private static final String EXCESSIVE_PARAMETER_LIST_VIOLATION_MSG =
            "java.design.ExcessiveParameterListRule.violation.msg";
}
