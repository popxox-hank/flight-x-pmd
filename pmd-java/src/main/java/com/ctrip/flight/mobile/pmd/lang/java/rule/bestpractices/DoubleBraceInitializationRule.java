package com.ctrip.flight.mobile.pmd.lang.java.rule.bestpractices;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-07-19
 */
public class DoubleBraceInitializationRule extends FlightXPathRule {

    private static final String XPATH = "//MethodDeclaration[not(starts-with(@Name, 'test') or ends-with" +
            "(@Name, 'Test') or ends-with(@Name, 'Tests') or ends-with(@Name, 'TestCase'))" +
            "]//PrimaryExpression//AllocationExpression/ClassOrInterfaceBody[count(*)=1]//Initializer[@Static=false" +
            "()]";


    public DoubleBraceInitializationRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }


    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, DOUBLE_BRACE_INITIALIZATION_VIOLATION_MSG);
    }

    private static final String DOUBLE_BRACE_INITIALIZATION_VIOLATION_MSG =
            "java.best.practices.DoubleBraceInitializationRule.violation.msg";
}
