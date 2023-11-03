package com.ctrip.flight.mobile.pmd.lang.java.rule.customization;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightCustomizationRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.properties.PropertyDescriptor;
import net.sourceforge.pmd.properties.PropertyFactory;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author haoren
 * Create at: 2023-10-26
 */
public class NarrativeParagraphRule extends FlightCustomizationRule {

    private SortedMap<Integer, Node> sortedNodeAndComment;
    private static final PropertyDescriptor<Integer> PROBLEM_PARAGRAPH_LIMIT_LINE_DESCRIPTOR
            = PropertyFactory.intProperty("paragraphLine")
            .desc("this is reports over than number of narrative paragraph")
            .defaultValue(10)
            .build();

    public NarrativeParagraphRule() {
        super();
        definePropertyDescriptor(PROBLEM_PARAGRAPH_LIMIT_LINE_DESCRIPTOR);
        addRuleChainVisit(ASTCompilationUnit.class);
        addRuleChainVisit(ASTMethodDeclaration.class);
    }

    @Override
    public Object visit(ASTMethodDeclaration node, Object data) {
        if (isTestClass || isTestMethod) {
            return data;
        }

        int paragraphLimitLine = getProperty(PROBLEM_PARAGRAPH_LIMIT_LINE_DESCRIPTOR);
        int commentLine = 0;
        int firstStatementLine = 0;
        int statementTotalLine = 0;
        int periodStatementEndline = 0;
        boolean isPeriodCommentNode = false;

        for (Map.Entry<Integer, Node> entry : sortedNodeAndComment.entrySet()) {
            Node value = entry.getValue();
            if (value.getBeginLine() <= node.getBeginLine()) {
                continue;
            }
            if (value.getBeginLine() > node.getEndLine()) {
                break;
            }

            if (value instanceof FormalComment || value instanceof MultiLineComment) {
                commentLine = value.getEndLine();
                statementTotalLine = 0;
                isPeriodCommentNode = true;
                firstStatementLine = 0;
                periodStatementEndline = 0;
            } else if (value instanceof SingleLineComment) {
                commentLine = value.getEndLine();
                statementTotalLine = 0;
                isPeriodCommentNode = true;
                firstStatementLine = 0;
                periodStatementEndline = 0;
            } else {
                boolean isContainBlankLine = (value.getBeginLine() - periodStatementEndline) > 1
                        && periodStatementEndline > 0;

                if (isContainBlankLine || isPeriodCommentNode) {
                    statementTotalLine = 1;
                    firstStatementLine = value.getBeginLine();
                } else {
                    statementTotalLine += value.getEndLine() - value.getBeginLine() + 1;
                }

                isPeriodCommentNode = false;
                periodStatementEndline = value.getEndLine();

                if (statementTotalLine > paragraphLimitLine
                        && (firstStatementLine - commentLine) > 1) {
                    addViolationWithPrecisePosition(data, node, NARRARIVE_PARAGRAPH_VIOLATION_MSG, paragraphLimitLine);
                    return data;
                }
            }
        }
        return data;
    }


    @Override
    public Object visit(ASTCompilationUnit node, Object data) {
        sortedNodeAndComment = totalCommentsAndExpressions(node);
        return super.visit(node, data);
    }

    private SortedMap<Integer, Node> totalCommentsAndExpressions(ASTCompilationUnit compilationUnit) {
        SortedMap<Integer, Node> itemsByLineNumber = new TreeMap<>();

        // expression nodes
        List<ASTExpression> expressionNodes = compilationUnit.findDescendantsOfType(ASTExpression.class);
        for (Node node : expressionNodes) {
            itemsByLineNumber.put(generateIndex(node), node);
        }

        for (Node node : compilationUnit.getComments()) {
            itemsByLineNumber.put(generateIndex(node), node);
        }

        return itemsByLineNumber;
    }

    private int generateIndex(Node node) {
        return (node.getBeginLine() << 16) + node.getBeginColumn();
    }

    private static final String NARRARIVE_PARAGRAPH_VIOLATION_MSG =
            "java.customization.NarrativeParagraphRule.violation.msg";
}
