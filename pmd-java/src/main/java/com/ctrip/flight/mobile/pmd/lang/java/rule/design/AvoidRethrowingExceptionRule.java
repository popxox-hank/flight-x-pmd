package com.ctrip.flight.mobile.pmd.lang.java.rule.design;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-07-24
 */
@Deprecated
public class AvoidRethrowingExceptionRule extends FlightXPathRule {

    private static final String XPATH = "//CatchStatement[FormalParameter" +
            "/VariableDeclaratorId/@Name = Block/BlockStatement/Statement" +
            "/ThrowStatement/Expression/PrimaryExpression[count(PrimarySuffix)=0]/PrimaryPrefix/Name/@Image" +
            " and count(Block/BlockStatement/Statement) =1]";

    public AvoidRethrowingExceptionRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, AVOID_RETHROWING_EXCEPTION_VIOLATION_MSG);
    }

    private static final String AVOID_RETHROWING_EXCEPTION_VIOLATION_MSG =
            "java.design.AvoidRethrowingExceptionRule.violation.msg";
}
