package com.ctrip.flight.mobile.pmd.lang.java.rule.errorprone;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-08-03
 */
public class EmptyCatchBlockRule extends FlightXPathRule {

    private static final String XPATH = "//CatchStatement[not(Block/BlockStatement)][$allowCommentedBlocks != true() " +
            "or Block/@containsComment = false()][FormalParameter/Type/ReferenceType/ClassOrInterfaceType[@Image != " +
            "'InterruptedException' and @Image != 'CloneNotSupportedException']][FormalParameter/VariableDeclaratorId" +
            "[not(matches(@Name, $allowExceptionNameRegex))]]";

    public EmptyCatchBlockRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, EMPTY_CATCH_BLOCK_VIOLATION_MSG);
    }

    private static final String EMPTY_CATCH_BLOCK_VIOLATION_MSG =
            "java.errorprone.EmptyCatchBlockRule.violation.msg";
}
