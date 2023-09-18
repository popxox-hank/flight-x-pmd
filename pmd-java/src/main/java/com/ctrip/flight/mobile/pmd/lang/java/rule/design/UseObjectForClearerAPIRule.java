package com.ctrip.flight.mobile.pmd.lang.java.rule.design;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-07-25
 */
@Deprecated
public class UseObjectForClearerAPIRule extends FlightXPathRule {

    private static final String XPATH = "//MethodDeclaration[@Public=true()]/MethodDeclarator/FormalParameters[count" +
            "(FormalParameter/Type/ReferenceType/ClassOrInterfaceType[@Image = 'String' and @Array=false()" +
            "]) > 5]";

    public UseObjectForClearerAPIRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, USE_OBJECT_FOR_CLEARER_API_VIOLATION_MSG);
    }

    private static final String USE_OBJECT_FOR_CLEARER_API_VIOLATION_MSG =
            "java.design.UseObjectForClearerAPIRule.violation.msg";
}
