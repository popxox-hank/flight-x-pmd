package com.ctrip.flight.mobile.pmd.lang.java.rule.design;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightJavaMetricsRule;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.lang.java.ast.internal.PrettyPrintingUtil;
import net.sourceforge.pmd.lang.java.metrics.JavaMetrics;
import net.sourceforge.pmd.lang.java.metrics.api.JavaClassMetricKey;
import net.sourceforge.pmd.lang.java.metrics.api.JavaOperationMetricKey;
import net.sourceforge.pmd.lang.java.metrics.impl.NcssMetric;
import net.sourceforge.pmd.lang.metrics.MetricOptions;
import net.sourceforge.pmd.lang.metrics.MetricsUtil;
import net.sourceforge.pmd.lang.metrics.ResultOption;
import net.sourceforge.pmd.properties.PropertyDescriptor;
import net.sourceforge.pmd.properties.PropertyFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static net.sourceforge.pmd.properties.constraints.NumericConstraints.positive;

/**
 * @author haoren
 * Create at: 2023-07-25
 */
public class NcssCountRule extends FlightJavaMetricsRule {


    private static final PropertyDescriptor<Integer> METHOD_REPORT_LEVEL_DESCRIPTOR =
            PropertyFactory.intProperty("methodReportLevel")
                    .desc("NCSS reporting threshold for methods")
                    .require(positive())
                    .defaultValue(60)
                    .build();

    private static final PropertyDescriptor<Integer> CLASS_REPORT_LEVEL_DESCRIPTOR =
            PropertyFactory.intProperty("classReportLevel")
                    .desc("NCSS reporting threshold for classes")
                    .require(positive())
                    .defaultValue(1500)
                    .build();

    private static final PropertyDescriptor<List<NcssMetric.NcssOption>> NCSS_OPTIONS_DESCRIPTOR;
    private int methodReportLevel;
    private int classReportLevel;
    private MetricOptions ncssOptions;


    static {
        Map<String, NcssMetric.NcssOption> options = new HashMap<>();
        options.put(NcssMetric.NcssOption.COUNT_IMPORTS.valueName(), NcssMetric.NcssOption.COUNT_IMPORTS);

        NCSS_OPTIONS_DESCRIPTOR = PropertyFactory.enumListProperty("ncssOptions", options)
                .desc("Choose options for the computation of Ncss")
                .emptyDefaultValue()
                .build();

    }


    public NcssCountRule() {
        definePropertyDescriptor(METHOD_REPORT_LEVEL_DESCRIPTOR);
        definePropertyDescriptor(CLASS_REPORT_LEVEL_DESCRIPTOR);
        definePropertyDescriptor(NCSS_OPTIONS_DESCRIPTOR);
    }


    @Override
    public Object visit(ASTCompilationUnit node, Object data) {
        methodReportLevel = getProperty(METHOD_REPORT_LEVEL_DESCRIPTOR);
        classReportLevel = getProperty(CLASS_REPORT_LEVEL_DESCRIPTOR);
        ncssOptions = MetricOptions.ofOptions(getProperty(NCSS_OPTIONS_DESCRIPTOR));

        super.visit(node, data);
        return data;
    }


    @Override
    public Object visit(ASTAnyTypeDeclaration node, Object data) {

        super.visit(node, data);

        if (JavaClassMetricKey.NCSS.supports(node)) {
            int classSize = (int) MetricsUtil.computeMetric(JavaClassMetricKey.NCSS, node, ncssOptions);
            int classHighest = (int) JavaMetrics.get(JavaOperationMetricKey.NCSS, node, ncssOptions,
                    ResultOption.HIGHEST);

            if (classSize >= classReportLevel) {
                addViolationWithPrecisePosition(data, node, NCSS_COUNT_CLASS_VIOLATION_MSG,
                        node.getTypeKind().name().toLowerCase(Locale.ROOT),
                        node.getImage(),
                        classSize,
                        classHighest,
                        classReportLevel);
            }
        }
        return data;
    }


    @Override
    public Object visit(ASTMethodOrConstructorDeclaration node, Object data) {

        if (JavaOperationMetricKey.NCSS.supports((MethodLikeNode) node)) {
            int methodSize = (int) MetricsUtil.computeMetric(JavaOperationMetricKey.NCSS, node, ncssOptions);
            if (methodSize >= methodReportLevel) {
                String violationMsg = node instanceof ASTMethodDeclaration
                        ? NCSS_COUNT_METHOD_VIOLATION_MSG
                        : NCSS_COUNT_CONSTRUCTOR_VIOLATION_MSG;
                addViolationWithPrecisePosition(data, node, violationMsg,
                        PrettyPrintingUtil.displaySignature(node),
                        String.valueOf(methodSize),
                        methodReportLevel);
            }
        }
        return data;
    }

    private static final String NCSS_COUNT_CLASS_VIOLATION_MSG =
            "java.design.NcssCountRule.Class.violation.msg";

    private static final String NCSS_COUNT_METHOD_VIOLATION_MSG =
            "java.design.NcssCountRule.Method.violation.msg";

    private static final String NCSS_COUNT_CONSTRUCTOR_VIOLATION_MSG =
            "java.design.NcssCountRule.Constructor.violation.msg";
}
