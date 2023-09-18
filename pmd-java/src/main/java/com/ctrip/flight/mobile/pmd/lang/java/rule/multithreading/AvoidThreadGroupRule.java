package com.ctrip.flight.mobile.pmd.lang.java.rule.multithreading;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-07-25
 */
public class AvoidThreadGroupRule extends FlightXPathRule {

    private static final String XPATH = "//AllocationExpression/ClassOrInterfaceType[pmd-java:typeIs('java.lang" +
            ".ThreadGroup')]|//PrimarySuffix[contains(@Image, 'getThreadGroup')]";

    public AvoidThreadGroupRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, AVOID_THREAD_GROUP_VIOLATION_MSG);
    }

    private static final String AVOID_THREAD_GROUP_VIOLATION_MSG =
            "java.multithreading.AvoidThreadGroupRule.violation.msg";
}
