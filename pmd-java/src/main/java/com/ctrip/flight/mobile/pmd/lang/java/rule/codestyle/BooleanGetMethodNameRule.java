package com.ctrip.flight.mobile.pmd.lang.java.rule.codestyle;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-07-20
 */
public class BooleanGetMethodNameRule extends FlightXPathRule {

    private static final String XPATH = "//MethodDeclaration[starts-with(@Name, 'get')]" +
            "[@Arity = 0 or $checkParameterizedMethods = true()][ResultType/Type/PrimitiveType[@Image = 'boolean'] " +
            "and not(../Annotation//Name[@Image = 'Override'])]";


    public BooleanGetMethodNameRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, BOOEAN_GET_METHOD_NAME_VIOLATION_MSG, node.getImage());
    }

    private static final String BOOEAN_GET_METHOD_NAME_VIOLATION_MSG =
            "java.code.style.BooleanGetMethodNameRule.violation.msg";
}
