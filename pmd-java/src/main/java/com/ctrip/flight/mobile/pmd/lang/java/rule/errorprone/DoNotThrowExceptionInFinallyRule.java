package com.ctrip.flight.mobile.pmd.lang.java.rule.errorprone;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-08-03
 */
public class DoNotThrowExceptionInFinallyRule extends FlightXPathRule {

    private static final String XPATH = "//FinallyStatement[descendant::ThrowStatement]";

    public DoNotThrowExceptionInFinallyRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, DO_NOT_THROW_EXCEPTION_IN_FINALLY_VIOLATION_MSG);
    }

    private static final String DO_NOT_THROW_EXCEPTION_IN_FINALLY_VIOLATION_MSG =
            "java.errorprone.DoNotThrowExceptionInFinallyRule.violation.msg";
}
