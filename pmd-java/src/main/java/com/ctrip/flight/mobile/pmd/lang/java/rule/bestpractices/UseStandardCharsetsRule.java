package com.ctrip.flight.mobile.pmd.lang.java.rule.bestpractices;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-07-20
 */
@Deprecated
public class UseStandardCharsetsRule extends FlightXPathRule {

    private static final String XPATH = "//PrimaryExpression[PrimaryPrefix/Name/@Image = 'Charset.forName']\n" +
            "    /PrimarySuffix/Arguments/ArgumentList/Expression/PrimaryExpression/PrimaryPrefix\n" +
            "        /Literal[@Image = ('\"US-ASCII\"', '\"ISO-8859-1\"', '\"UTF-8\"', '\"UTF-16BE\"', " +
            "'\"UTF-16LE\"', '\"UTF-16\"')]";


    public UseStandardCharsetsRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, USE_STANDARD_CHARSET_VIOLATION_MSG);
    }

    private static final String USE_STANDARD_CHARSET_VIOLATION_MSG =
            "java.best.practices.UseStandardCharsetsRule.violation.msg";
}
