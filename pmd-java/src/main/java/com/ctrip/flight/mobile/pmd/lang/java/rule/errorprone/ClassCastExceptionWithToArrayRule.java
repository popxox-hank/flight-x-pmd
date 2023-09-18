package com.ctrip.flight.mobile.pmd.lang.java.rule.errorprone;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-08-03
 */
@Deprecated
public class ClassCastExceptionWithToArrayRule extends FlightXPathRule {

    private static final String XPATH = "//CastExpression[Type/ReferenceType/ClassOrInterfaceType[@Image != " +
            "\"Object\"]]/PrimaryExpression[PrimaryPrefix/Name[ends-with(@Image, '.toArray')" +
            "]][PrimarySuffix/Arguments[not(*)]][count(PrimarySuffix) = 1]";

    public ClassCastExceptionWithToArrayRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, CLASS_CASE_EXCEPTION_WITH_TO_ARRAY_VIOLATION_MSG);
    }

    private static final String CLASS_CASE_EXCEPTION_WITH_TO_ARRAY_VIOLATION_MSG =
            "java.errorprone.ClassCastExceptionWithToArrayRule.violation.msg";
}
