package com.ctrip.flight.mobile.pmd.lang.java.rule.errorprone;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-08-03
 */
public class CloneMethodReturnTypeMustMatchClassNameRule extends FlightXPathRule {

    private static final String XPATH = "//MethodDeclaration[@Name = 'clone' and @Arity = 0 and not " +
            "(ResultType//ClassOrInterfaceType/@Image = ancestor::ClassOrInterfaceDeclaration[1]/@SimpleName)]";

    public CloneMethodReturnTypeMustMatchClassNameRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, CLONE_METHOD_RETURN_TYPE_MUST_MATCH_CLASS_NAME_VIOLATION_MSG);
    }

    private static final String CLONE_METHOD_RETURN_TYPE_MUST_MATCH_CLASS_NAME_VIOLATION_MSG =
            "java.errorprone.CloneMethodReturnTypeMustMatchClassNameRule.violation.msg";
}
