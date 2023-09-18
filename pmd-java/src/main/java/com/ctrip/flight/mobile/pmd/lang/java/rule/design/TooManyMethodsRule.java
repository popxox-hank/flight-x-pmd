package com.ctrip.flight.mobile.pmd.lang.java.rule.design;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-07-25
 */
public class TooManyMethodsRule extends FlightXPathRule {

    private static final String XPATH = "//ClassOrInterfaceDeclaration/ClassOrInterfaceBody[count(" +
            "./ClassOrInterfaceBodyDeclaration/MethodDeclaration[not ((starts-with(@Name,'get') or starts-with(@Name," +
            "'set') or starts-with(@Name,'is')) and count(Block/BlockStatement) <= 1)]) > $maxmethods]";

    public TooManyMethodsRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, TOO_MANY_METHODS_VIOLATION_MSG);
    }

    private static final String TOO_MANY_METHODS_VIOLATION_MSG =
            "java.design.TooManyMethodsRule.violation.msg";
}
