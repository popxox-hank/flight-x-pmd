package com.ctrip.flight.mobile.pmd.lang.java.rule.codestyle;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-07-21
 */
public class PackageCaseRule extends FlightXPathRule {

    private static final String XPATH = "//PackageDeclaration/Name[lower-case(@Image)!=@Image]";


    public PackageCaseRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, PACKAGE_CASE_VIOLATION_MSG, node.getImage());
    }

    private static final String PACKAGE_CASE_VIOLATION_MSG =
            "java.code.style.PackageCaseRule.violation.msg";
}
