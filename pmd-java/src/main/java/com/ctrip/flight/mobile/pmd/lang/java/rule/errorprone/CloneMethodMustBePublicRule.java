package com.ctrip.flight.mobile.pmd.lang.java.rule.errorprone;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-08-03
 */
public class CloneMethodMustBePublicRule extends FlightXPathRule {

    private static final String XPATH = "//MethodDeclaration[@Public= false()][@Name = 'clone'][@Arity = 0]";

    public CloneMethodMustBePublicRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, CLONE_METHOD_MUST_BE_PUBLIC_VIOLATION_MSG);
    }

    private static final String CLONE_METHOD_MUST_BE_PUBLIC_VIOLATION_MSG =
            "java.errorprone.CloneMethodMustBePublicRule.violation.msg";
}
