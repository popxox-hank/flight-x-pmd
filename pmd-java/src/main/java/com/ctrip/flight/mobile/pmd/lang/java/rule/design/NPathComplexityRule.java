package com.ctrip.flight.mobile.pmd.lang.java.rule.design;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightJavaMetricsRule;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodOrConstructorDeclaration;
import net.sourceforge.pmd.lang.java.ast.internal.PrettyPrintingUtil;
import net.sourceforge.pmd.lang.java.metrics.api.JavaOperationMetricKey;
import net.sourceforge.pmd.lang.metrics.MetricsUtil;
import net.sourceforge.pmd.properties.PropertyDescriptor;
import net.sourceforge.pmd.properties.PropertyFactory;

import java.util.logging.Logger;

import static net.sourceforge.pmd.properties.constraints.NumericConstraints.positive;

/**
 * @author haoren
 * Create at: 2023-07-25
 */
public class NPathComplexityRule extends FlightJavaMetricsRule {

    private static final Logger LOG = Logger.getLogger(NPathComplexityRule.class.getName());

    @Deprecated
    private static final PropertyDescriptor<Double> MINIMUM_DESCRIPTOR
            = PropertyFactory.doubleProperty("minimum").desc("Deprecated! Minimum reporting threshold")
            .require(positive()).defaultValue(200d).build();


    private static final PropertyDescriptor<Integer> REPORT_LEVEL_DESCRIPTOR
            = PropertyFactory.intProperty("reportLevel").desc("N-Path Complexity reporting threshold")
            .require(positive()).defaultValue(200).build();


    private int reportLevel = 200;


    public NPathComplexityRule() {
        definePropertyDescriptor(REPORT_LEVEL_DESCRIPTOR);
        definePropertyDescriptor(MINIMUM_DESCRIPTOR);
    }


    @Override
    public Object visit(ASTCompilationUnit node, Object data) {
        reportLevel = getReportLevel();

        super.visit(node, data);
        return data;
    }


    private int getReportLevel() {
        double oldProp = getProperty(MINIMUM_DESCRIPTOR);
        if (oldProp != MINIMUM_DESCRIPTOR.defaultValue()) {
            LOG.warning("Rule NPathComplexity uses deprecated property 'minimum'. Future versions of PMD will remove " +
                    "support for this property. Please use 'reportLevel' instead!");
            return (int) oldProp;
        }

        return getProperty(REPORT_LEVEL_DESCRIPTOR);
    }


    @Override
    public final Object visit(ASTMethodOrConstructorDeclaration node, Object data) {
        if (!JavaOperationMetricKey.NPATH.supports(node)) {
            return data;
        }

        int npath = (int) MetricsUtil.computeMetric(JavaOperationMetricKey.NPATH, node);
        if (npath >= reportLevel) {
            String violationMsg = node instanceof ASTMethodDeclaration
                    ? N_PATH_COMPLEXITY_METHOD_VIOLATION_MSG
                    : N_PATH_COMPLEXITY_CONSTRUCTOR_VIOLATION_MSG;
            addViolationWithPrecisePosition(data, node, violationMsg,
                    PrettyPrintingUtil.displaySignature(node),
                    String.valueOf(npath),
                    String.valueOf(reportLevel));
        }

        return data;
    }

    private static final String N_PATH_COMPLEXITY_METHOD_VIOLATION_MSG =
            "java.design.NPathComplexityRule.Method.violation.msg";

    private static final String N_PATH_COMPLEXITY_CONSTRUCTOR_VIOLATION_MSG =
            "java.design.NPathComplexityRule.Constructor.violation.msg";
}
