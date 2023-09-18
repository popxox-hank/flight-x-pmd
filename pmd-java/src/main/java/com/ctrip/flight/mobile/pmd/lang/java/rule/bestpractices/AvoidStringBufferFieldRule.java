package com.ctrip.flight.mobile.pmd.lang.java.rule.bestpractices;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-07-18
 */
public class AvoidStringBufferFieldRule extends FlightXPathRule {

    private static final String XPATH = "//FieldDeclaration/ClassOrInterfaceType[pmd-java:typeIs" +
            "('java.lang.StringBuffer') or pmd-java:typeIs('java.lang.StringBuilder')]";

    public AvoidStringBufferFieldRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, AVOID_STRING_BUFFER_FILED_VIOLATION_MSG, node.getImage());
    }

    private static final String AVOID_STRING_BUFFER_FILED_VIOLATION_MSG =
            "java.best.practices.AvoidStringBufferFieldRule.violation.msg";
}
