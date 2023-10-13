package com.ctrip.flight.mobile.pmd.lang.java.rule.customization;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightStreamExpressionRule;
import net.sourceforge.pmd.lang.java.ast.ASTAssignmentOperator;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryExpression;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryPrefix;
import net.sourceforge.pmd.lang.java.ast.JavaNode;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author haoren
 * Create at: 2023-08-09
 */
public class StreamExpressionStyleRule extends FlightStreamExpressionRule {


    public StreamExpressionStyleRule() {
        super();
        addRuleChainVisit(ASTPrimaryExpression.class);
    }

    @Override
    public Object visit(ASTPrimaryExpression node, Object data) {
        if (isTestClass || isTestMethod) {
            return data;
        }

        setLocalStreamVariableName(node);
        if (isStreamExpressionAndSetStreamInfo(node)
                && isStreamExpressionViolation(node)) {
            addViolationWithPrecisePosition(data, node, STREAM_EXPRESSION_STYLE_VIOLATION_MSG);
        }
        return data;

    }

    private boolean isStreamExpressionViolation(ASTPrimaryExpression node) {
        String imageName;
        int baseStreamLine = getBaseSteamLine(node);
        for (JavaNode childNode : node.children()) {
            imageName = getPrimaryExpressionImageName(childNode);
            if (isStreamExpressionViolation(imageName, baseStreamLine, childNode, node)) {
                return true;
            }
        }
        return false;
    }


    private boolean isStreamExpressionViolation(String imageName,
                                                int baseStreamLine,
                                                JavaNode currentNode,
                                                JavaNode expressionNode) {
        // imageName空代表是空节点(AST语法树结构中会把括号也单独作为一个节点)
        if (StringUtils.isEmpty(imageName)) {
            return false;
        }

        // 当前节点是stream表达式的首节点 不去比对
        if (currentNode instanceof ASTPrimaryPrefix) {
            return false;
        }

        // 获取整个stream表达式中被判定为stream的变量的索引节点
        int streamIndex = getCurrentStreamIndex(expressionNode);

        // 当前节点的索引值小于等于streamIndex则不进行判断
        if (currentNode.jjtGetChildIndex() <= streamIndex) {
            return false;
        }

        // 判断是否是不需要检查违规的节点Optional.get().getXXX不是一个流表达式
        if (unCheckViolation(expressionNode)) {
            return false;
        }

        // 流表达式其他的语句是否有同行的场景 因为是检索整个流结构，所以需要剔除prefix及相同的自身的比较
        for (int i = 0; i < expressionNode.getNumChildren(); i++) {
            if (expressionNode.getChild(i) instanceof ASTPrimaryPrefix
                    || StringUtils.isEmpty(expressionNode.getChild(i).getImage())
                    || i <= streamIndex) {
                continue;
            }

            // 当检测的是节点自身时候需要确认流表达式所在行数是否和当前的行数一致
            if (expressionNode.getChild(i).jjtGetChildIndex() == currentNode.jjtGetChildIndex()) {
                if (isAssignmentOperator(currentNode) || isStreamMethodVariable(expressionNode)) {
                    continue;
                }
                if (baseStreamLine == currentNode.getEndLine() || baseStreamLine == currentNode.getBeginLine()) {
                    return true;
                } else {
                    continue;
                }

            }

            if (expressionNode.getChild(i).getBeginLine() == currentNode.getBeginLine()
                    || expressionNode.getChild(i).getEndLine() == currentNode.getEndLine()) {
                return true;
            }
            // 可能存在某个流的lambda操作有换行，然后再紧跟一个流，这个时候的上一个流的beginLine和endLine和当前的检索的流是在不同行的，所以需要通过column来补充
            // 如：.filter(x->
            //      x==3).filter(x->....)
            if (expressionNode.getChild(i).getBeginColumn() != currentNode.getBeginColumn()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 防止model中的流赋值语句this.stream = stream被检测出
     *
     * @param node
     * @return
     */
    private boolean isAssignmentOperator(JavaNode node) {
        if (Objects.isNull(node.getParent())
                || Objects.isNull(node.getParent().getParent())) {
            return false;
        }
        for (JavaNode javaNode : node.getParent().getParent().children()) {
            if (javaNode instanceof ASTAssignmentOperator) {
                return true;
            }
        }
        return false;
    }

    private int getCurrentStreamIndex(JavaNode expressionNode) {
        for (JavaNode javaNode : expressionNode.children()) {
            int index = streamInfoList.stream()
                    .filter(x -> StringUtils.equalsIgnoreCase(javaNode.getImage(), x.getImageName()))
                    .map(StreamInfo::getStreamIndex)
                    .findFirst()
                    .orElse(0);
            if (index > 0) {
                return index;
            }
        }
        return 0;
    }

    /**
     * 获取流表达式所在行
     *
     * @param node
     * @return
     */
    private int getBaseSteamLine(ASTPrimaryExpression node) {
        String imageName;
        for (int i = 0; i < node.getNumChildren(); i++) {
            imageName = getPrimaryExpressionImageName(node.getChild(i));
            if (isStream(imageName)) {
                return node.getChild(i).getEndLine();
            }
        }
        return 0;
    }

    private static final String STREAM_EXPRESSION_STYLE_VIOLATION_MSG =
            "java.customization.StreamExpressionStyleRule.violation.msg";

}
