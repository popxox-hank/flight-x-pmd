package com.ctrip.flight.mobile.pmd.lang.java.rule.performance;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-07-26
 */
public class TooFewBranchesForASwitchStatementRule extends FlightXPathRule {

    private static final String XPATH = "//SwitchStatement[(count(.//SwitchLabel) < $minimumNumberCaseForASwitch)]";

    public TooFewBranchesForASwitchStatementRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, TOO_FEW_BRANCHES_FOR_SWITCH_STATEMENT_VIOLATION_MSG);
    }

    private static final String TOO_FEW_BRANCHES_FOR_SWITCH_STATEMENT_VIOLATION_MSG =
            "java.performance.TooFewBranchesForASwitchStatementRule.violation.msg";
}
