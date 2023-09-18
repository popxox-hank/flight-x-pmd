package com.ctrip.flight.mobile.pmd.lang.java.rule.errorprone;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-08-04
 */
public class ReturnFromFinallyBlockRule extends FlightXPathRule {

    private static final String XPATH = "//FinallyStatement//ReturnStatement except //FinallyStatement//" +
            "(MethodDeclaration|LambdaExpression)//ReturnStatement";

    public ReturnFromFinallyBlockRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, RETURN_FROM_FINALLY_BLOCK_VIOLATION_MSG);
    }

    private static final String RETURN_FROM_FINALLY_BLOCK_VIOLATION_MSG =
            "java.errorprone.ReturnFromFinallyBlockRule.violation.msg";
}
