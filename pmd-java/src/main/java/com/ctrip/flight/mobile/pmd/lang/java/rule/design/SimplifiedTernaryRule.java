package com.ctrip.flight.mobile.pmd.lang.java.rule.design;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-07-25
 */
public class SimplifiedTernaryRule extends FlightXPathRule {

    private static final String XPATH = "//ConditionalExpression[(Expression|.)" +
            "/PrimaryExpression/*/Literal/BooleanLiteral and count((Expression|.)/PrimaryExpression/*/Literal/BooleanLiteral)=2][not((Expression|.)" +
            "/PrimaryExpression/*/Literal/NullLiteral)]";

    public SimplifiedTernaryRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, SIMPLIFIED_TERNARY_VIOLATION_MSG);
    }

    private static final String SIMPLIFIED_TERNARY_VIOLATION_MSG =
            "java.design.SimplifiedTernaryRule.violation.msg";
}
