package com.ctrip.flight.mobile.pmd.lang.java.rule.performance;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-07-26
 */
public class AddEmptyStringRule extends FlightXPathRule {

    private static final String XPATH = "//AdditiveExpression/PrimaryExpression/PrimaryPrefix/Literal[@Image='\"\"' " +
            "and not(ancestor::Annotation)]|//AdditiveExpression/PrimaryExpression/PrimaryPrefix/Name[@Image = " +
            "(//FieldDeclaration[@Final = true()]|ancestor::MethodDeclaration//LocalVariableDeclaration[@Final = true" +
            "()])" +
            "/VariableDeclarator[@Initializer = true()" +
            "][VariableInitializer/Expression/PrimaryExpression/PrimaryPrefix/Literal[@Image" +
            "='\"\"']]/VariableDeclaratorId/@Name]";

    public AddEmptyStringRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, ADD_EMPTY_STRING_VIOLATION_MSG);
    }

    private static final String ADD_EMPTY_STRING_VIOLATION_MSG =
            "java.performance.AddEmptyStringRule.violation.msg";
}
