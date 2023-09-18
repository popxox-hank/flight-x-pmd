package com.ctrip.flight.mobile.pmd.lang.java.rule.errorprone;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-08-04
 */
public class ReturnEmptyCollectionRatherThanNullRule extends FlightXPathRule {

    private static final String XPATH = "//ReturnStatement/Expression/PrimaryExpression/PrimaryPrefix/Literal" +
            "/NullLiteral[ancestor::MethodDeclaration[1][(./ResultType/Type[pmd-java:typeIs('java.util.Collection') " +
            "or pmd-java:typeIs('java.util.Map') or @ArrayType=true()])]][not(./ancestor::LambdaExpression)]";

    public ReturnEmptyCollectionRatherThanNullRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, RETURN_EMPTY_COLLECTION_RATHER_THAN_NULL_VIOLATION_MSG);
    }

    private static final String RETURN_EMPTY_COLLECTION_RATHER_THAN_NULL_VIOLATION_MSG =
            "java.errorprone.ReturnEmptyCollectionRatherThanNullRule.violation.msg";
}
