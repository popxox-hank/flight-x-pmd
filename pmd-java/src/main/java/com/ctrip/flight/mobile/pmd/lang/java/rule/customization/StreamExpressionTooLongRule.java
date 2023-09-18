package com.ctrip.flight.mobile.pmd.lang.java.rule.customization;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightStreamExpressionRule;
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
 * Create at: 2023-08-24
 */
public class StreamExpressionTooLongRule extends FlightStreamExpressionRule {


    private SortedMap<Integer, Node> sortedNodeAndComment;

    private static final PropertyDescriptor<Integer> PROBLEM_STREAM_EXPRESS_MAX_LINE_DESCRIPTOR
            = PropertyFactory.intProperty("maxLine")
            .desc("This is the maximum number of rows for reporting stream expressions")
            .defaultValue(30)
            .build();

    public StreamExpressionTooLongRule() {
        super();
        definePropertyDescriptor(PROBLEM_STREAM_EXPRESS_MAX_LINE_DESCRIPTOR);
        addRuleChainVisit(ASTCompilationUnit.class);
        addRuleChainVisit(ASTPrimaryExpression.class);
    }

    @Override
    public Object visit(ASTCompilationUnit node, Object data) {
        sortedNodeAndComment = totalCommentsAndExpressions(node);
        return super.visit(node, data);
    }

    @Override
    public Object visit(ASTPrimaryExpression node, Object data) {
        setContainStreamVariableName(node);
        if (!isStreamExpression(node,false)) {
            return data;
        }
        final int streamExpressionMaxLine = getProperty(PROBLEM_STREAM_EXPRESS_MAX_LINE_DESCRIPTOR);
        int beginLine = node.getBeginLine();
        int endLine = node.getEndLine();
        int commentLineCount = getCommentLineCount(node);
        if ((endLine - beginLine - commentLineCount + 1) > streamExpressionMaxLine) {
            addViolationWithPrecisePosition(data, node, STREAM_EXPRESSION_TOO_LONG_VIOLATION_MSG);
        }
        return data;
    }


    private int getCommentLineCount(ASTPrimaryExpression node) {
        int lineCount = 0;
        AbstractJavaNode lastNode = null;

        for (Map.Entry<Integer, Node> entry : sortedNodeAndComment.entrySet()) {
            Node value = entry.getValue();
            if (value.getBeginLine() <= node.getBeginLine()) {
                continue;
            }
            if (value.getBeginLine() > node.getEndLine()) {
                break;
            }

            // value should be either expression or comment.
            if (value instanceof AbstractJavaNode) {
                lastNode = (AbstractJavaNode) value;
            } else if (value instanceof FormalComment || value instanceof MultiLineComment) {
                Comment comment = (Comment) value;
                lineCount += comment.getEndLine() - comment.getBeginLine() + 1;
            } else if (value instanceof SingleLineComment) {
                SingleLineComment singleLineComment = (SingleLineComment) value;
                // Comment may in the same line with node.
                if (lastNode == null || singleLineComment.getBeginLine() != lastNode.getBeginLine()) {
                    lineCount += 1;
                }
            }
        }
        return lineCount;
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

    private static final String STREAM_EXPRESSION_TOO_LONG_VIOLATION_MSG =
            "java.customization.StreamExpressionTooLongRule.violation.msg";
}
