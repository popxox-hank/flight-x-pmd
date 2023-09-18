package com.ctrip.flight.mobile.pmd.lang.java.rule.errorprone;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-08-03
 */
@Deprecated
public class AvoidLiteralsInIfConditionRule extends FlightXPathRule {

    private static final String XPATH = "//IfStatement[$ignoreExpressions = true()" +
            "]/Expression/*/PrimaryExpression/PrimaryPrefix/Literal[not(NullLiteral)][not(BooleanLiteral)][empty" +
            "(index-of(tokenize($ignoreMagicNumbers, '\\s*,\\s*'), @Image))]|//IfStatement[$ignoreExpressions = false" +
            "()]/Expression//*[local-name() != 'UnaryExpression' or @Operator" +
            " != '-']/PrimaryExpression/PrimaryPrefix/Literal[not(NullLiteral)][not(BooleanLiteral)][empty(index-of" +
            "(tokenize($ignoreMagicNumbers, '\\s*,\\s*'), @Image))]|//IfStatement[$ignoreExpressions = false()" +
            "]/Expression//UnaryExpression[@Operator = '-']/PrimaryExpression/PrimaryPrefix/Literal[not(NullLiteral)" +
            "][not(BooleanLiteral)][empty(index-of(tokenize($ignoreMagicNumbers, '\\s*,\\s*'), concat('-', @Image)))" +
            "]|//IfStatement[$ignoreExpressions = false()]/Expression[count" +
            "(*/PrimaryExpression/PrimaryPrefix/Literal[not(NullLiteral)][not(BooleanLiteral)]) > 1]";

    public AvoidLiteralsInIfConditionRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, AVOID_LITERALS_IN_IF_CONDITION_VIOLATION_MSG);
    }

    private static final String AVOID_LITERALS_IN_IF_CONDITION_VIOLATION_MSG =
            "java.errorprone.AvoidLiteralsInIfConditionRule.violation.msg";
}
