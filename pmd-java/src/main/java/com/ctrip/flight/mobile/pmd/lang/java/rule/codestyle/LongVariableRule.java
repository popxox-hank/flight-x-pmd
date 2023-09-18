package com.ctrip.flight.mobile.pmd.lang.java.rule.codestyle;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-07-21
 */
public class LongVariableRule extends FlightXPathRule {

    private static final String XPATH = "//VariableDeclaratorId[string-length(@Name) > $minimum]";


    public LongVariableRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, LONG_VARIABLE_VIOLATION_MSG, node.getImage());
    }

    private static final String LONG_VARIABLE_VIOLATION_MSG =
            "java.code.style.LongVariableRule.violation.msg";
}
