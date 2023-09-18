package com.ctrip.flight.mobile.pmd.lang.java.rule.performance;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-07-26
 */
public class AvoidFileStreamRule extends FlightXPathRule {

    private static final String XPATH = "//PrimaryPrefix/AllocationExpression/ClassOrInterfaceType[pmd-java:typeIs" +
            "('java.io.FileInputStream') or pmd-java:typeIs('java.io.FileOutputStream') or pmd-java:typeIs('java.io" +
            ".FileReader') or pmd-java:typeIs('java.io.FileWriter')]";

    public AvoidFileStreamRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, AVOID_FILE_STREAM_VIOLATION_MSG);
    }

    private static final String AVOID_FILE_STREAM_VIOLATION_MSG =
            "java.performance.AvoidFileStreamRule.violation.msg";
}
