package com.ctrip.flight.mobile.pmd.lang.java.rule.codestyle;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-07-20
 */
@Deprecated
public class EmptyMethodInAbstractClassShouldBeAbstractRule extends FlightXPathRule {

    private static final String XPATH = "//ClassOrInterfaceDeclaration[@Abstract = true()]/ClassOrInterfaceBody" +
            "/ClassOrInterfaceBodyDeclaration/MethodDeclaration[@Abstract = false() and @Native = false()]" +
            "[( boolean(./Block[count(./BlockStatement) =  1]" +
            "/BlockStatement/Statement/ReturnStatement/Expression/PrimaryExpression/PrimaryPrefix/Literal" +
            "/NullLiteral) = true() ) or ( boolean(./Block[count(./BlockStatement) = 1]" +
            "/BlockStatement/Statement/ReturnStatement/Expression/PrimaryExpression/PrimaryPrefix/Literal[@Image = " +
            "'0']) = true() ) or ( boolean(./Block[count(./BlockStatement) = 1]" +
            "/BlockStatement/Statement/ReturnStatement/Expression/PrimaryExpression/PrimaryPrefix/Literal[string" +
            "-length(@Image) = 2]) = true() ) or (./Block[count(./BlockStatement) =  " +
            "1]/BlockStatement/Statement/EmptyStatement) or ( not (./Block/*) )]";


    public EmptyMethodInAbstractClassShouldBeAbstractRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, EMPTY_METHOD_IN_ABSTRACT_CLASS_SHOULD_BE_ABSTRACT_VIOLATION_MSG);
    }

    private static final String EMPTY_METHOD_IN_ABSTRACT_CLASS_SHOULD_BE_ABSTRACT_VIOLATION_MSG =
            "java.code.style.EmptyMethodInAbstractClassShouldBeAbstractRule.violation.msg";
}
