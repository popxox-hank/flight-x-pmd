package com.ctrip.flight.mobile.pmd.lang.java.rule.design;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-07-25
 */
public class SimplifyBooleanExpressionsRule extends FlightXPathRule {

    private static final String XPATH = "//EqualityExpression/PrimaryExpression/PrimaryPrefix/Literal/BooleanLiteral";

    public SimplifyBooleanExpressionsRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, SIMPLIFIED_BOOLEAN_EXPRESSIONS_VIOLATION_MSG);
    }

    private static final String SIMPLIFIED_BOOLEAN_EXPRESSIONS_VIOLATION_MSG =
            "java.design.SimplifyBooleanExpressionsRule.violation.msg";
}
