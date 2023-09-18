package com.ctrip.flight.mobile.pmd.lang.java.rule.errorprone;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-08-03
 */
public class MissingSerialVersionUIDRule extends FlightXPathRule {

    private static final String XPATH = "//ClassOrInterfaceDeclaration[@Interface = false()][count" +
            "(ClassOrInterfaceBody/ClassOrInterfaceBodyDeclaration/FieldDeclaration/VariableDeclarator" +
            "/VariableDeclaratorId[@Name='serialVersionUID']) = 0][(ImplementsList | ExtendsList)" +
            "/ClassOrInterfaceType[pmd-java:typeIs('java.io.Serializable')]]";

    public MissingSerialVersionUIDRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, MISSING_SERIAL_VERSION_UID_VIOLATION_MSG);
    }

    private static final String MISSING_SERIAL_VERSION_UID_VIOLATION_MSG =
            "java.errorprone.MissingSerialVersionUIDRule.violation.msg";
}
