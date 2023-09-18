package com.ctrip.flight.mobile.pmd.lang.java.rule.performance;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-07-26
 */
public class OptimizableToArrayCallRule extends FlightXPathRule {

    private static final String XPATH = "//PrimaryExpression[PrimaryPrefix/Name[ends-with(@Image, 'toArray')" +
            "]][/PrimaryExpression/PrimaryPrefix/AllocationExpression/ArrayDimsAndInits/Expression/PrimaryExpression" +
            "/PrimaryPrefix[not(Literal[@Image='0'])]]";

    public OptimizableToArrayCallRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, OPTIMIZABLE_TO_ARRAY_CALL_VIOLATION_MSG);
    }

    private static final String OPTIMIZABLE_TO_ARRAY_CALL_VIOLATION_MSG =
            "java.performance.OptimizableToArrayCallRule.violation.msg";
}
