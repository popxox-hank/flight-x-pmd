package com.ctrip.flight.mobile.pmd.lang.java.rule.bestpractices;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-07-19
 */
public class ForLoopVariableCountRule extends FlightXPathRule {

    private static final String XPATH = "//ForInit/LocalVariableDeclaration[count(VariableDeclarator) > $maximumVariables]";


    public ForLoopVariableCountRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, FOR_LOOP_VARIABLE_COUNT_VIOLATION_MSG);
    }

    private static final String FOR_LOOP_VARIABLE_COUNT_VIOLATION_MSG =
            "java.best.practices.ForLoopVariableCountRule.violation.msg";
}
