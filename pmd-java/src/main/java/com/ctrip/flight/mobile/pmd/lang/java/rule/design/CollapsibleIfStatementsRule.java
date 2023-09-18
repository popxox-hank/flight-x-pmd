package com.ctrip.flight.mobile.pmd.lang.java.rule.design;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-07-24
 */
public class CollapsibleIfStatementsRule extends FlightXPathRule {

    private static final String XPATH = "//IfStatement[@Else= false()]/Statement/IfStatement[@Else= false()]|" +
            "//IfStatement[@Else= false()]/Statement/Block[count(BlockStatement)=1]/BlockStatement" +
            "/Statement/IfStatement[@Else= false()]";

    public CollapsibleIfStatementsRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, COLLAPSIBLE_IF_STATEMENTS_VIOLATION_MSG);
    }

    private static final String COLLAPSIBLE_IF_STATEMENTS_VIOLATION_MSG =
            "java.design.CollapsibleIfStatementsRule.violation.msg";
}
