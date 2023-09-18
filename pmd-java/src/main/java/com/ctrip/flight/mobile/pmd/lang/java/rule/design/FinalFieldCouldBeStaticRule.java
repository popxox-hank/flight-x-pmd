package com.ctrip.flight.mobile.pmd.lang.java.rule.design;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-07-24
 */
public class FinalFieldCouldBeStaticRule extends FlightXPathRule {

    private static final String XPATH = "//FieldDeclaration\n" +
            " [@Final=true() and @Static=false()]\n" +
            " [not(preceding-sibling::Annotation/MarkerAnnotation/Name[@Image=\"Builder.Default\"]\n" +
            "    and //ImportDeclaration/Name[@Image=\"lombok.Builder\"])]\n" +
            "/VariableDeclarator\n" +
            " [VariableInitializer/Expression/PrimaryExpression[not(PrimarySuffix)]\n" +
            "  /PrimaryPrefix/*\n" +
            "    [\n" +
            "        self::Literal (: literal :)\n" +
            "        or\n" +
            "        (: another static field :)\n" +
            "        self::Name[@Image=//FieldDeclaration[@Static=true()]/VariableDeclarator/@Name]\n" +
            "        or\n" +
            "        (:unnecessary parenthesis :)\n" +
            "        self::Expression/PrimaryExpression/PrimaryPrefix/Literal\n" +
            "        or\n" +
            "        (:empty array allocation :)\n" +
            "        self::AllocationExpression[ArrayDimsAndInits/Expression/PrimaryExpression/PrimaryPrefix/*\n" +
            "            [\n" +
            "                self::Literal[@IntLiteral = true()][@Image=\"0\"]\n" +
            "                or\n" +
            "                self::Expression/PrimaryExpression/PrimaryPrefix/Literal[@IntLiteral = true()" +
            "][@Image=\"0\"]\n" +
            "            ]\n" +
            "        ]\n" +
            "    ]\n" +
            " ]\n" +
            "/VariableDeclaratorId\n" +
            "  [not(@Image = //MethodDeclaration[@Static = false()" +
            "]//SynchronizedStatement/Expression/PrimaryExpression/\n" +
            "      (PrimaryPrefix/Name|PrimarySuffix[preceding-sibling::PrimaryPrefix[@ThisModifier = true()]])" +
            "/@Image)]";

    public FinalFieldCouldBeStaticRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node,
                FINAL_FIELD_COULD_BE_STATIC_VIOLATION_MSG, node.getImage());
    }

    private static final String FINAL_FIELD_COULD_BE_STATIC_VIOLATION_MSG =
            "java.design.FinalFieldCouldBeStaticRule.violation.msg";
}
