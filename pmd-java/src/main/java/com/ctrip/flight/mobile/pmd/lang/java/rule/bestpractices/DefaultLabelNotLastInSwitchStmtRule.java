package com.ctrip.flight.mobile.pmd.lang.java.rule.bestpractices;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-07-19
 */
public class DefaultLabelNotLastInSwitchStmtRule extends FlightXPathRule {

    private static final String XPATH = "//SwitchLabel[@Default = true() and not(.. is ../../*[last()])]";


    public DefaultLabelNotLastInSwitchStmtRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, DEFAULT_LABEL_NOT_LAST_IN_SWITCH_VIOLATION_MSG);
    }

    private static final String DEFAULT_LABEL_NOT_LAST_IN_SWITCH_VIOLATION_MSG =
            "java.best.practices.DefaultLabelNotLastInSwitchStmtRule.violation.msg";
}
