package com.ctrip.flight.mobile.pmd.lang.java.rule.design;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-07-25
 */
public class SimplifyConditionalRule extends FlightXPathRule {

    private static final String XPATH = "//Expression\n" +
            " [ConditionalOrExpression\n" +
            " [EqualityExpression[@Image='==']\n" +
            "  //NullLiteral\n" +
            "  and\n" +
            "  UnaryExpressionNotPlusMinus\n" +
            "   [@Image='!']//InstanceOfExpression[PrimaryExpression\n" +
            "     //Name/@Image = ancestor::ConditionalOrExpression/EqualityExpression\n" +
            "      /PrimaryExpression/PrimaryPrefix/Name/@Image]\n" +
            "  and\n" +
            "  (count(UnaryExpressionNotPlusMinus) + 1 = count(*))\n" +
            " ]\n" +
            "or\n" +
            "ConditionalAndExpression\n" +
            " [EqualityExpression[@Image='!=']//NullLiteral\n" +
            " and\n" +
            "InstanceOfExpression\n" +
            " [PrimaryExpression[not(PrimarySuffix[@ArrayDereference= true()])]\n" +
            "  //Name[not(contains(@Image,'.'))]/@Image = ancestor::ConditionalAndExpression\n" +
            "   /EqualityExpression/PrimaryExpression/PrimaryPrefix/Name/@Image]\n" +
            " and\n" +
            "(count(InstanceOfExpression) + 1 = count(*))\n" +
            " ]\n" +
            "]";

    public SimplifyConditionalRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, SIMPLIFIED_CONDTITINAL_VIOLATION_MSG);
    }

    private static final String SIMPLIFIED_CONDTITINAL_VIOLATION_MSG =
            "java.design.SimplifyConditionalRule.violation.msg";
}
