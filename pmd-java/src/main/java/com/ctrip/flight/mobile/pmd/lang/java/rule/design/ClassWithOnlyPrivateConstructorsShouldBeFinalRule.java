package com.ctrip.flight.mobile.pmd.lang.java.rule.design;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-07-24
 */
@Deprecated
public class ClassWithOnlyPrivateConstructorsShouldBeFinalRule extends FlightXPathRule {

    private static final String XPATH = "//TypeDeclaration(: no lombok constructor annotations :)" +
            "[not(Annotation[pmd-java:typeIs('lombok.Value')])]" +
            "[not(Annotation[pmd-java:typeIs('lombok.NoArgsConstructor') or pmd-java:typeIs('lombok" +
            ".RequiredArgsConstructor') or pmd-java:typeIs('lombok.AllArgsConstructor')][not(" +
            ".//MemberValuePair[@MemberName='access']) or .//MemberValuePair[@MemberName='access'][" +
            ".//Name[@Image!='PRIVATE'][@Image!='AccessLevel.PRIVATE'][@Image!='lombok.AccessLevel.PRIVATE']]])]" +
            "/ClassOrInterfaceDeclaration[@Final = false()](: at least one private constructor :)" +
            "[ClassOrInterfaceBody/ClassOrInterfaceBodyDeclaration/ConstructorDeclaration[@Private = true()]](: no " +
            "public constructor :) [not(./ClassOrInterfaceBody/ClassOrInterfaceBodyDeclaration/ConstructorDeclaration" +
            "[(@Public = true()) or (@Protected = true()) or (@PackagePrivate = true())])](: not a base class in the" +
            " same compilation unit :)[not(@SimpleName = ../." +
            "./TypeDeclaration/ClassOrInterfaceDeclaration/ExtendsList/ClassOrInterfaceType" +
            "/@Image)](: not a base class for an inner class in the same compilation unit :)[not(@SimpleName = " +
            ".//ClassOrInterfaceDeclaration/ExtendsList/ClassOrInterfaceType/@Image)]";

    public ClassWithOnlyPrivateConstructorsShouldBeFinalRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node,
                CLASS_WITH_ONLY_PRIVATE_CONSTRUCTORS_SHOULD_BE_FINAL_VIOLATION_MSG, node.getImage());
    }

    private static final String CLASS_WITH_ONLY_PRIVATE_CONSTRUCTORS_SHOULD_BE_FINAL_VIOLATION_MSG =
            "java.design.ClassWithOnlyPrivateConstructorsShouldBeFinalRule.violation.msg";
}
