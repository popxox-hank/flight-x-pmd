package com.ctrip.flight.mobile.pmd.lang.java.rule.bestpractices;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-07-19
 */
public class ConstantInterfaceRule extends FlightXPathRule {

    private static final String XPATH = "//ClassOrInterfaceDeclaration[@Interface = true()]" +
            "[$ignoreIfHasMethods= false() or not(ClassOrInterfaceBody/MethodDeclaration)]" +
            "/ClassOrInterfaceBody/FieldDeclaration";


    public ConstantInterfaceRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, CONSTANT_INTERFACE_VIOLATION_MSG);
    }

    private static final String CONSTANT_INTERFACE_VIOLATION_MSG =
            "java.best.practices.ConstantInterfaceRule.violation.msg";
}
