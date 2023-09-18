package com.ctrip.flight.mobile.pmd.lang.java.rule.codestyle;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-07-20
 */
@Deprecated
public class AvoidProtectedFieldInFinalClassRule extends FlightXPathRule {

    private static final String XPATH = "//ClassOrInterfaceDeclaration[@Final= true()]" +
            "/ClassOrInterfaceBody/ClassOrInterfaceBodyDeclaration" +
            "/FieldDeclaration[@Protected= true()]";


    public AvoidProtectedFieldInFinalClassRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, AVOID_PROTECTED_FIELD_IN_FINAL_CLASS_VIOLATION_MSG,
                node.getImage());
    }

    private static final String AVOID_PROTECTED_FIELD_IN_FINAL_CLASS_VIOLATION_MSG =
            "java.code.style.AvoidProtectedFieldInFinalClassRule.violation.msg";
}
