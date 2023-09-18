package com.ctrip.flight.mobile.pmd.lang.java.rule.design;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightJavaRule;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTIfStatement;
import net.sourceforge.pmd.properties.PropertyDescriptor;
import net.sourceforge.pmd.properties.PropertyFactory;

import static net.sourceforge.pmd.properties.constraints.NumericConstraints.positive;

/**
 * @author haoren
 * Create at: 2023-07-24
 */
public class AvoidDeeplyNestedIfStmtsRule extends FlightJavaRule {

    private int depth;
    private int depthLimit;

    private static final PropertyDescriptor<Integer> PROBLEM_DEPTH_DESCRIPTOR
            = PropertyFactory.intProperty("problemDepth")
            .desc("The if statement depth reporting threshold")
            .require(positive()).defaultValue(3).build();

    public AvoidDeeplyNestedIfStmtsRule() {
        definePropertyDescriptor(PROBLEM_DEPTH_DESCRIPTOR);
    }

    @Override
    public Object visit(ASTCompilationUnit node, Object data) {
        depth = 0;
        depthLimit = getProperty(PROBLEM_DEPTH_DESCRIPTOR);
        return super.visit(node, data);
    }

    @Override
    public Object visit(ASTIfStatement node, Object data) {
        if (!node.hasElse()) {
            depth++;
        }
        super.visit(node, data);
        if (depth == depthLimit) {
            addViolationWithPrecisePosition(data, node, AVOID_DEEPLY_NESTED_IF_STMTS_VIOLATION_MSG);
        }
        depth--;
        return data;
    }

    private static final String AVOID_DEEPLY_NESTED_IF_STMTS_VIOLATION_MSG =
            "java.design.AvoidDeeplyNestedIfStmtsRule.violation.msg";
}
