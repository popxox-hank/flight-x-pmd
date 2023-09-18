package com.ctrip.flight.mobile.pmd.lang.java.rule.codestyle;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-07-20
 */
@Deprecated
public class AvoidProtectedMethodInFinalClassNotExtendingRule extends FlightXPathRule {

    private static final String XPATH = "//ClassOrInterfaceDeclaration[@Final= true() and not(ExtendsList)]" +
            "/ClassOrInterfaceBody/ClassOrInterfaceBodyDeclaration/MethodDeclaration[@Protected=true() and @Name != 'finalize']";


    public AvoidProtectedMethodInFinalClassNotExtendingRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, AVOID_PROTECTED_METHOD_IN_FINAL_CLASS_VIOLATION_MSG,
                node.getImage());
    }

    private static final String AVOID_PROTECTED_METHOD_IN_FINAL_CLASS_VIOLATION_MSG =
            "java.code.style.AvoidProtectedMethodInFinalClassNotExtendingRule.violation.msg";
}
