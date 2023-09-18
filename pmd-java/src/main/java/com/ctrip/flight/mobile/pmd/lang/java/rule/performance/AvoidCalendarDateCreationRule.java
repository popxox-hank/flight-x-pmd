package com.ctrip.flight.mobile.pmd.lang.java.rule.performance;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-07-26
 */
public class AvoidCalendarDateCreationRule extends FlightXPathRule {

    private static final String XPATH = "//PrimaryPrefix[Name[ends-with(@Image, 'Calendar.getInstance')]] [count(." +
            "./PrimarySuffix) > 2 and ../PrimarySuffix[last()-1][@Image = 'getTime' or " +
            "@Image='getTimeInMillis']]|//MethodDeclaration[not" +
            "(MethodDeclarator/FormalParameters//ClassOrInterfaceType[pmd-java:typeIs('java" +
            ".util.Calendar')])]/Block/BlockStatement//PrimaryExpression/PrimaryPrefix/Name[pmd-java:typeIs('java" +
            ".util.Calendar')][every $var in @Image satisfies ((ends-with($var, '.getTime') or ends-with($var, '" +
            ".getTimeInMillis')) and (: ignore if .set* or .add* or .clear is called on the variable :) not" +
            " (ancestor::Block/BlockStatement//Name[starts-with(@Image, concat((tokenize($var, '\\.'), $var)[1], '" +
            ".set')) or starts-with(@Image, concat((tokenize($var, '\\.'), $var)[1], '.add')) or starts-with(@Image, " +
            "concat((tokenize($var, '\\.'), $var)[1], '.clear'))]))]|//ClassOrInterfaceType[pmd-java:typeIs('org.joda" +
            ".time.DateTime') or pmd-java:typeIs('org.joda.time" +
            ".LocalDateTime')][../Arguments/ArgumentList/Expression/PrimaryExpression/PrimaryPrefix/Name[ends-with" +
            "(@Image, 'Calendar.getInstance')]]";

    public AvoidCalendarDateCreationRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, AVOID_CALENDAR_DATE_CREATION_VIOLATION_MSG);
    }

    private static final String AVOID_CALENDAR_DATE_CREATION_VIOLATION_MSG =
            "java.performance.AvoidCalendarDateCreationRule.violation.msg";
}
