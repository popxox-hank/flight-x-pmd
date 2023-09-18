package com.ctrip.flight.mobile.pmd.lang.java.rule.design;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightJavaMetricsRule;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodOrConstructorDeclaration;
import net.sourceforge.pmd.lang.java.ast.internal.PrettyPrintingUtil;
import net.sourceforge.pmd.lang.metrics.MetricsUtil;
import net.sourceforge.pmd.properties.PropertyDescriptor;
import net.sourceforge.pmd.properties.PropertyFactory;

import static net.sourceforge.pmd.lang.java.metrics.api.JavaOperationMetricKey.COGNITIVE_COMPLEXITY;
import static net.sourceforge.pmd.properties.constraints.NumericConstraints.positive;

/**
 * @author haoren
 * Create at: 2023-07-24
 */
public class CognitiveComplexityRule extends FlightJavaMetricsRule {

    private static final PropertyDescriptor<Integer> REPORT_LEVEL_DESCRIPTOR
            = PropertyFactory.intProperty("reportLevel").desc("Cognitive Complexity reporting threshold")
            .require(positive()).defaultValue(15).build();

    public CognitiveComplexityRule() {
        definePropertyDescriptor(REPORT_LEVEL_DESCRIPTOR);
    }

    private int getReportLevel() {
        return getProperty(REPORT_LEVEL_DESCRIPTOR);
    }

    @Override
    public final Object visit(ASTMethodOrConstructorDeclaration node, Object data) {
        if (!COGNITIVE_COMPLEXITY.supports(node)) {
            return data;
        }

        int cognitive = (int) MetricsUtil.computeMetric(COGNITIVE_COMPLEXITY, node);
        final int reportLevel = getReportLevel();
        if (cognitive >= reportLevel) {
            String violationMsg = node instanceof ASTMethodDeclaration
                    ? COGNITIVE_COMPLEXITY_METHOD_VIOLATION_MSG
                    : COGNITIVE_COMPLEXITY_CONSTRUCTOR_VIOLATION_MSG;
            addViolationWithPrecisePosition(data, node, violationMsg,
                    PrettyPrintingUtil.displaySignature(node),
                    String.valueOf(cognitive),
                    String.valueOf(reportLevel));
        }

        return data;
    }

    private static final String COGNITIVE_COMPLEXITY_METHOD_VIOLATION_MSG =
            "java.design.CognitiveComplexityRule.Method.violation.msg";

    private static final String COGNITIVE_COMPLEXITY_CONSTRUCTOR_VIOLATION_MSG =
            "java.design.CognitiveComplexityRule.Constructor.violation.msg";
}
