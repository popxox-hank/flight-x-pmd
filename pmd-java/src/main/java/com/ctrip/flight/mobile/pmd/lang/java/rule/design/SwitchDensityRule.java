package com.ctrip.flight.mobile.pmd.lang.java.rule.design;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightStatisticalJavaRule;
import net.sourceforge.pmd.lang.java.ast.ASTStatement;
import net.sourceforge.pmd.lang.java.ast.ASTSwitchLabel;
import net.sourceforge.pmd.lang.java.ast.ASTSwitchStatement;
import net.sourceforge.pmd.stat.DataPoint;

/**
 * @author haoren
 * Create at: 2023-07-25
 */
public class SwitchDensityRule extends FlightStatisticalJavaRule {


    private static class SwitchDensity {
        private int labels = 0;
        private int stmts = 0;

        public void addSwitchLabel() {
            labels++;
        }

        public void addStatement() {
            stmts++;
        }

        public void addStatements(int stmtCount) {
            stmts += stmtCount;
        }

        public int getStatementCount() {
            return stmts;
        }

        public double getDensity() {
            if (labels == 0) {
                return 0;
            }
            return (double) stmts / (double) labels;
        }
    }

    public SwitchDensityRule() {
        setProperty(MINIMUM_DESCRIPTOR, 10d);
    }

    @Override
    public Object visit(ASTSwitchStatement node, Object data) {
        SwitchDensity oldData = null;

        if (data instanceof SwitchDensity) {
            oldData = (SwitchDensity) data;
        }

        SwitchDensity density = new SwitchDensity();

        super.visit(node, density);

        DataPoint point = new DataPoint();
        point.setNode(node);
        point.setScore(density.getDensity());
        point.setMessage(SWITCH_DENSITY_VIOLATION_MSG);

        addDataPoint(point);

        if (data instanceof SwitchDensity) {
            ((SwitchDensity) data).addStatements(density.getStatementCount());
        }
        return oldData;
    }

    @Override
    public Object visit(ASTStatement statement, Object data) {
        if (data instanceof SwitchDensity) {
            ((SwitchDensity) data).addStatement();
        }

        return super.visit(statement, data);
    }

    @Override
    public Object visit(ASTSwitchLabel switchLabel, Object data) {
        if (data instanceof SwitchDensity) {
            ((SwitchDensity) data).addSwitchLabel();
        }

        return super.visit(switchLabel, data);
    }

    private static final String SWITCH_DENSITY_VIOLATION_MSG =
            "java.design.SwitchDensityRule.violation.msg";
}
