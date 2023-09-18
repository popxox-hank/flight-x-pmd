package com.ctrip.flight.mobile.pmd.lang.java.rule.design;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-07-24
 */
@Deprecated
public class AvoidThrowingNewInstanceOfSameExceptionRule extends FlightXPathRule {

    private static final String XPATH = "//CatchStatement[count(Block/BlockStatement/Statement) = 1 and" +
            " FormalParameter/Type/ReferenceType/ClassOrInterfaceType/@Image = " +
            "Block/BlockStatement/Statement/ThrowStatement/Expression/PrimaryExpression/PrimaryPrefix" +
            "/AllocationExpression/ClassOrInterfaceType/@Image and " +
            "count(Block/BlockStatement/Statement/ThrowStatement/Expression/PrimaryExpression/PrimaryPrefix" +
            "/AllocationExpression/Arguments/ArgumentList/Expression) = 1 and FormalParameter/VariableDeclaratorId = " +
            "Block/BlockStatement/Statement/ThrowStatement/Expression/PrimaryExpression/PrimaryPrefix" +
            "/AllocationExpression/Arguments/ArgumentList/Expression/PrimaryExpression/PrimaryPrefix/Name]";

    public AvoidThrowingNewInstanceOfSameExceptionRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, AVOID_THROWING_NEW_INSTANCE_OF_SAME_EXCEPTION_VIOLATION_MSG);
    }

    private static final String AVOID_THROWING_NEW_INSTANCE_OF_SAME_EXCEPTION_VIOLATION_MSG =
            "java.design.AvoidThrowingNewInstanceOfSameExceptionRule.violation.msg";
}
