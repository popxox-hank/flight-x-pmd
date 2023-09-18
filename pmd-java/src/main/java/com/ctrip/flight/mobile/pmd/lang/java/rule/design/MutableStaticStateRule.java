package com.ctrip.flight.mobile.pmd.lang.java.rule.design;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-07-25
 */
public class MutableStaticStateRule extends FlightXPathRule {

    private static final String XPATH = "//FieldDeclaration[@Static = true() and @Private = false() and @Final = " +
            "false()]";

    public MutableStaticStateRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, MUTABLE_STATIC_STATE_VIOLATION_MSG);
    }

    private static final String MUTABLE_STATIC_STATE_VIOLATION_MSG =
            "java.design.MutableStaticStateRule.violation.msg";
}
