package com.ctrip.flight.mobile.pmd.lang.java.rule.codestyle;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-07-20
 */
public class ControlStatementBracesRule extends FlightXPathRule {

    private static final String XPATH = "//WhileStatement[$checkWhileStmt and not(Statement/Block) and not" +
            "($allowEmptyLoop and Statement/EmptyStatement)]|" +
            "//ForStatement[$checkForStmt and not(Statement/Block) and not($allowEmptyLoop and Statement/EmptyStatement)]" +
            "|//DoStatement[$checkDoWhileStmt and not(Statement/Block) and not($allowEmptyLoop and " +
            "Statement/EmptyStatement)] |(: The violation is reported on the sub statement -- not the if statement :)" +
            "//Statement[$checkIfElseStmt and parent::IfStatement and not(child::Block or child::IfStatement)" +
            "(: Whitelists single if statements :) and ($checkSingleIfStmt (: Inside this not(...) is the definition " +
            "of a \"single if statement\" " +
            "or not(count(../Statement) = 1 (: No else stmt :)(: Not the last branch of an 'if ... else if' chain :) " +
            "and not(parent::IfStatement[parent::Statement[parent::IfStatement]])))]" +
            "|(: Reports case labels if one of their subordinate statements is not braced :)" +
            "//SwitchLabel[$checkCaseStmt][count(following-sibling::BlockStatement except " +
            "following-sibling::SwitchLabel[1]/following-sibling::BlockStatement) > 1 or (some $stmt (: in only the " +
            "block statements until the next label :) in following-sibling::BlockStatement except " +
            "following-sibling::SwitchLabel[1]/following-sibling::BlockStatement satisfies not($stmt/Statement/Block)" +
            ")]";


    public ControlStatementBracesRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, CONTROL_STATEMENT_BRACES_VIOLATION_MSG);
    }

    private static final String CONTROL_STATEMENT_BRACES_VIOLATION_MSG =
            "java.code.style.ControlStatementBracesRule.violation.msg";
}
