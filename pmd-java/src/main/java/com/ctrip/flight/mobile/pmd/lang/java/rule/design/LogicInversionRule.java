package com.ctrip.flight.mobile.pmd.lang.java.rule.design;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-07-25
 */
public class LogicInversionRule extends FlightXPathRule {

    private static final String XPATH = "//UnaryExpressionNotPlusMinus[@Image='!']/PrimaryExpression/PrimaryPrefix" +
            "/Expression[EqualityExpression or RelationalExpression]";

    public LogicInversionRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, LOGIC_INVERSION_VIOLATION_MSG);
    }

    private static final String LOGIC_INVERSION_VIOLATION_MSG =
            "java.design.LogicInversionRule.violation.msg";
}
