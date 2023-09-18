package com.ctrip.flight.mobile.pmd.lang.java.rule.customization;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightStreamExpressionRule;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.properties.PropertyDescriptor;
import net.sourceforge.pmd.properties.PropertyFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author haoren
 * Create at: 2023-09-06
 */
public class AvoidStreamExpressionInStreamExpressionRule extends FlightStreamExpressionRule {

    private int streamExpressionNum;


    private static final PropertyDescriptor<Integer> PROBLEM_STREAM_EXPRESS_LIMIT_NUM_DESCRIPTOR
            = PropertyFactory.intProperty("streamExpressionLimitNum")
            .desc("this is reports the number of stream expressions contained in stream expressions")
            .defaultValue(0)
            .build();

    public AvoidStreamExpressionInStreamExpressionRule() {
        super();
        definePropertyDescriptor(PROBLEM_STREAM_EXPRESS_LIMIT_NUM_DESCRIPTOR);
        addRuleChainVisit(ASTPrimaryExpression.class);
        addRuleChainVisit(ASTLambdaExpression.class);


    }

    @Override
    public Object visit(ASTPrimaryExpression node, Object data) {
        setContainStreamVariableName(node);
        return data;
    }

    @Override
    public Object visit(ASTLambdaExpression node, Object data) {
        if (!isParentStreamExpression(node)) {
            return data;
        }
        List<ASTExpression> astExpressionList = getAstExpressionList(node);
        streamExpressionNum = 0;
        if (!astExpressionList.isEmpty()) {
            countStreamExpressionNum(astExpressionList);
        }

        final int streamExpressionLimitNum = getProperty(PROBLEM_STREAM_EXPRESS_LIMIT_NUM_DESCRIPTOR);
        if (streamExpressionNum > streamExpressionLimitNum) {
            addViolationWithPrecisePosition(data, node, AVOID_STREAM_EXPRESSION_IN_STREAM_EXPRESSION_VIOLATION_MSG);
        }

        return data;
    }

    /**
     * 计算lambda表达式中流表达式的数量
     *
     * @param astExpressionList
     */
    private void countStreamExpressionNum(List<ASTExpression> astExpressionList) {
        for (JavaNode javaNode : astExpressionList) {
            loopStreamExpressionNum(javaNode);
        }
    }

    private void loopStreamExpressionNum(JavaNode javaNode) {
        for (int i = 0; i < javaNode.getNumChildren(); i++) {
            if (javaNode.getChild(i) instanceof ASTPrimaryExpression
                    && isStreamExpression((ASTPrimaryExpression) javaNode.getChild(i), true)) {
                streamExpressionNum++;
            }
        }

        for (int i = 0; i < javaNode.getNumChildren(); i++) {
            loopStreamExpressionNum(javaNode.getChild(i));
        }

    }


    private List<ASTExpression> getAstExpressionList(ASTLambdaExpression node) {
        List<ASTExpression> astExpressionList = new ArrayList<>();
        for (int i = 0; i < node.getNumChildren(); i++) {
            if (node.getChild(i) instanceof ASTExpression) {
                astExpressionList.add((ASTExpression) node.getChild(i));
            }
        }
        return astExpressionList;
    }

    /**
     * 查询这个lambda表达式是否是ASTArgumentList（stream表达式中的lambda才会表现为参数形式)
     *
     * @param node
     * @return
     */
    private boolean isParentStreamExpression(ASTLambdaExpression node) {
        boolean isParent = true;
        JavaNode parentNode = node.getParent();
        while (isParent) {
            if (Objects.isNull(parentNode)) {
                isParent = false;
            }

            if (parentNode instanceof ASTArgumentList) {
                return true;
            }
            if (isParent) {
                parentNode = parentNode.getParent();
            }
        }

        return false;

    }

    private static final String AVOID_STREAM_EXPRESSION_IN_STREAM_EXPRESSION_VIOLATION_MSG =
            "java.customization.AvoidStreamExpressionInStreamExpressionRule.violation.msg";

}
