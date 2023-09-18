package com.ctrip.flight.mobile.pmd.lang.java.rule.errorprone;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-08-03
 */
public class CompareObjectsWithEqualsRule extends FlightXPathRule {

    private static final String XPATH = "//EqualityExpression[count(PrimaryExpression[pmd-java:typeIs('java.lang" +
            ".Object')][not(some $t in $typesThatCompareByReference satisfies pmd-java:typeIs($t))]) = 2][not" +
            "(PrimaryExpression[PrimaryPrefix/@ThisModifier = true()][not(PrimarySuffix)" +
            "][ancestor::MethodDeclaration[@Name = 'equals']])][not(PrimaryExpression[not(PrimarySuffix) and " +
            "PrimaryPrefix/Name[upper-case(@Image)=@Image] or PrimaryExpression/PrimarySuffix[last()][upper-case" +
            "(@Image)=@Image]])]";

    public CompareObjectsWithEqualsRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, COMARE_OBJECT_WITH_EQUALS_VIOLATION_MSG);
    }

    private static final String COMARE_OBJECT_WITH_EQUALS_VIOLATION_MSG =
            "java.errorprone.CompareObjectsWithEqualsRule.violation.msg";
}
