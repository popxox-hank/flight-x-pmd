package com.ctrip.flight.mobile.pmd.lang.java.rule.errorprone;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-08-04
 */
@Deprecated
public class UnconditionalIfStatementRule extends FlightXPathRule {

    private static final String XPATH = "//IfStatement/Expression[count(PrimaryExpression)" +
            "=1]/PrimaryExpression/PrimaryPrefix/Literal/BooleanLiteral";

    public UnconditionalIfStatementRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, UN_CONDITIONAL_IF_STATEMENT_VIOLATION_MSG);
    }

    private static final String UN_CONDITIONAL_IF_STATEMENT_VIOLATION_MSG =
            "java.errorprone.UnconditionalIfStatementRule.violation.msg";
}
