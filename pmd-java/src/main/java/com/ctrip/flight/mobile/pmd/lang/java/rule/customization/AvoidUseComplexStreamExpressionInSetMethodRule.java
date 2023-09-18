package com.ctrip.flight.mobile.pmd.lang.java.rule.customization;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightStreamExpressionRule;
import com.google.common.collect.Lists;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.properties.PropertyDescriptor;
import net.sourceforge.pmd.properties.PropertyFactory;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author haoren
 * Create at: 2023-09-14
 */
public class AvoidUseComplexStreamExpressionInSetMethodRule extends FlightStreamExpressionRule {

    private static final PropertyDescriptor<List<String>> PROBLEM_START_WITH_METHOD_NAME_DESCRIPTOR
            = PropertyFactory.stringListProperty("startWithMethodName")
            .desc("this is reports the method start with literals need handle")
            .defaultValue(Lists.newArrayList("set"))
            .build();
    private boolean isContainStream;

    public AvoidUseComplexStreamExpressionInSetMethodRule() {
        super();
        definePropertyDescriptor(PROBLEM_START_WITH_METHOD_NAME_DESCRIPTOR);
        addRuleChainVisit(ASTPrimaryExpression.class);
        addRuleChainVisit(ASTArguments.class);
    }

    @Override
    public Object visit(ASTPrimaryExpression node, Object data) {
        setContainStreamVariableName(node);
        return data;
    }

    @Override
    public Object visit(ASTArguments node, Object data) {
        if (isNeedHandle(node) && isContainStreamExpression(node)) {
            addViolationWithPrecisePosition(data, node,
                    AVOID_USE_COMPLEX_STREAM_EXPRESSION_IN_SET_METHOD_VIOLATION_MSG);
        }
        return data;
    }

    private boolean isContainStreamExpression(ASTArguments node) {
        List<ASTExpression> expressionList = node.findChildrenOfType(ASTArgumentList.class)
                .stream()
                .filter(Objects::nonNull)
                .map(argument -> argument.findChildrenOfType(ASTExpression.class))
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (expressionList.isEmpty()) {
            return false;
        }

        if (isDirectSetStream(expressionList)) {
            return false;
        }

        isContainStream = false;

        for (int i = 0; i < expressionList.size(); i++) {
            loopCheckStreamExpression(expressionList.get(i));
        }

        return isContainStream;
    }

    private void loopCheckStreamExpression(JavaNode node) {
        if (isContainStream) {
            return;
        }
        String imageName;
        for (int i = 0; i < node.getNumChildren(); i++) {
            imageName = getPrimaryExpressionImageName(node.getChild(i));
            // 如果stream的来源是入参、内部变量或者方法，则需要特殊处理
            if (isStreamName(imageName) || (isStreamVariable(imageName)
                    && !isSpecialStreamExpression(node))) {
                isContainStream = true;
                return;
            }
        }
        for (int i = 0; i < node.getNumChildren(); i++) {
            loopCheckStreamExpression(node.getChild(i));
        }
    }

    /**
     * 判断是否直接set流，如果是的话不告警
     *
     * @param expressionList
     * @return
     */
    private boolean isDirectSetStream(List<ASTExpression> expressionList) {
        List<List<ASTPrimaryExpression>> primaryExpressionList = expressionList.stream()
                .filter(Objects::nonNull)
                .map(x -> x.findDescendantsOfType(ASTPrimaryExpression.class))
                .collect(Collectors.toList());
        if (primaryExpressionList.isEmpty()) {
            return false;
        }
        for (List<ASTPrimaryExpression> astPrimaryList : primaryExpressionList) {
            for (ASTPrimaryExpression astPrimaryExpression : astPrimaryList) {
                if (astPrimaryExpression.getNumChildren() > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 判断是否是配置中设置的方法前缀
     *
     * @param node
     * @return
     */
    private boolean isNeedHandle(ASTArguments node) {
        List<ASTPrimaryExpression> primaryExpressionList = node.getParentsOfType(ASTPrimaryExpression.class);
        if (primaryExpressionList.isEmpty()) {
            return false;
        }
        List<ASTName> astNameList = primaryExpressionList
                .stream()
                .map(x -> x.findChildrenOfType(ASTPrimaryPrefix.class))
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .map(x -> x.findChildrenOfType(ASTName.class))
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (astNameList.isEmpty()) {
            return false;
        }

        final List<String> startWithMethodNameLsit = getProperty(PROBLEM_START_WITH_METHOD_NAME_DESCRIPTOR);
        List<String> imageNameList = astNameList.stream()
                .filter(x -> StringUtils.isNotEmpty(x.getImage()))
                .map(x -> getStreamImageNameList(x.getImage()))
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (startWithMethodNameLsit.isEmpty()
                || imageNameList.isEmpty()) {
            return false;
        }

        return startWithMethodNameLsit
                .stream()
                .anyMatch(x -> matchStartName(x, imageNameList));

    }

    private boolean matchStartName(String methodStartName, List<String> imageNameList) {
        if (StringUtils.isEmpty(methodStartName)) {
            return false;
        }
        return imageNameList
                .stream()
                .filter(Objects::nonNull)
                .map(imageName -> imageName.length() >= methodStartName.length()
                        ? imageName.substring(0, methodStartName.length())
                        : imageName)
                .anyMatch(startWithName -> StringUtils.equalsIgnoreCase(startWithName, methodStartName));
    }

    private static final String AVOID_USE_COMPLEX_STREAM_EXPRESSION_IN_SET_METHOD_VIOLATION_MSG =
            "java.customization.AvoidUseComplexStreamExpressionInSetMethodRule.violation.msg";
}
