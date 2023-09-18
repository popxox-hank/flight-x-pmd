package com.ctrip.flight.mobile.pmd.lang.java.rule.errorprone;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-08-02
 */
public class AvoidDecimalLiteralsInBigDecimalConstructorRule extends FlightXPathRule {

    private static final String XPATH = "//AllocationExpression[pmd-java:typeIs('java.math.BigDecimal')" +
            "][Arguments/ArgumentList/Expression/PrimaryExpression[pmd-java:typeIs('float') or pmd-java:typeIs('java" +
            ".lang.Float') or pmd-java:typeIs('double') or pmd-java:typeIs('java.lang.Double') ]]";

    public AvoidDecimalLiteralsInBigDecimalConstructorRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, AVOID_DECIMAL_LITERALS_IN_BIGDECIMAL_CONSTRUCTOR_VIOLATION_MSG);
    }

    private static final String AVOID_DECIMAL_LITERALS_IN_BIGDECIMAL_CONSTRUCTOR_VIOLATION_MSG =
            "java.errorprone.AvoidDecimalLiteralsInBigDecimalConstructorRule.violation.msg";
}
