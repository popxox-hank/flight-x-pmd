package com.ctrip.flight.mobile.pmd.lang.java.rule.codestyle;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-07-21
 */
public class GenericsNamingRule extends FlightXPathRule {

    private static final String XPATH = "//TypeDeclaration/ClassOrInterfaceDeclaration/TypeParameters/TypeParameter" +
            "[upper-case(@Image) != @Image]";


    public GenericsNamingRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, GENERICS_NAMING_VIOLATION_MSG);
    }

    private static final String GENERICS_NAMING_VIOLATION_MSG =
            "java.code.style.GenericsNamingRule.violation.msg";
}
