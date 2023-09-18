package com.ctrip.flight.mobile.pmd.lang.java.rule.errorprone;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-08-03
 */
@Deprecated
public class ImplicitSwitchFallThroughRule extends FlightXPathRule {

    private static final String XPATH = "//SwitchStatement[(count(.//BreakStatement) + count" +
            "(BlockStatement//Statement/ReturnStatement) + count(BlockStatement//Statement/ContinueStatement) + count" +
            "(BlockStatement//Statement/ThrowStatement) + count(BlockStatement//Statement/IfStatement[@Else= true() " +
            "and " +
            "Statement[2][ReturnStatement|ContinueStatement|ThrowStatement]]/Statement[1][ReturnStatement" +
            "|ContinueStatement|ThrowStatement]) + count(SwitchLabel[ following-sibling::node()[1][self::SwitchLabel]" +
            " ]) + count(SwitchLabel[count(following-sibling::node()) = 0]) < count (SwitchLabel))] or " +
            "(//MethodDeclaration//PrimaryExpression[PrimaryPrefix/Name[@Image='case']) and //LambdaExpression)";

    public ImplicitSwitchFallThroughRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, IMPLICIT_SWITCH_FALL_THROUGH_VIOLATION_MSG);
    }

    private static final String IMPLICIT_SWITCH_FALL_THROUGH_VIOLATION_MSG =
            "java.errorprone.ImplicitSwitchFallThroughRule.violation.msg";
}
