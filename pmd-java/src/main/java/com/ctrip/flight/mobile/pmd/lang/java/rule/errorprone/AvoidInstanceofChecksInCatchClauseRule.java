package com.ctrip.flight.mobile.pmd.lang.java.rule.errorprone;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-08-03
 */
public class AvoidInstanceofChecksInCatchClauseRule extends FlightXPathRule {

    private static final String XPATH = "//CatchStatement/FormalParameter/following-sibling::Block" +
            "//InstanceOfExpression/PrimaryExpression/PrimaryPrefix/Name[@Image = " +
            "./ancestor::Block/preceding-sibling::FormalParameter/VariableDeclaratorId/@Name]";

    public AvoidInstanceofChecksInCatchClauseRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, AVOID_INSTANCE_OF_CHECK_IN_CATCH_CLAUSE_VIOLATION_MSG);
    }

    private static final String AVOID_INSTANCE_OF_CHECK_IN_CATCH_CLAUSE_VIOLATION_MSG =
            "java.errorprone.AvoidInstanceofChecksInCatchClauseRule.violation.msg";
}
