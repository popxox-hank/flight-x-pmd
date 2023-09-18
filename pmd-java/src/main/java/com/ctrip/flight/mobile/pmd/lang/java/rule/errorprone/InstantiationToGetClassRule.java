package com.ctrip.flight.mobile.pmd.lang.java.rule.errorprone;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-08-03
 */
@Deprecated
public class InstantiationToGetClassRule extends FlightXPathRule {

    private static final String XPATH = "//PrimarySuffix[@Image='getClass'][parent::PrimaryExpression[PrimaryPrefix" +
            "/AllocationExpression][count(PrimarySuffix) = 2]]";

    public InstantiationToGetClassRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, INSTANTIATION_TO_GET_CLASS_VIOLATION_MSG);
    }

    private static final String INSTANTIATION_TO_GET_CLASS_VIOLATION_MSG =
            "java.errorprone.InstantiationToGetClassRule.violation.msg";
}
