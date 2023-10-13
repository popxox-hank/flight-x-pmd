package com.ctrip.flight.mobile.pmd.lang.java.rule.customization;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightCustomizationRule;
import com.google.common.collect.Lists;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.properties.PropertyDescriptor;
import net.sourceforge.pmd.properties.PropertyFactory;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author haoren
 * Create at: 2023-09-19
 */
public class AvoidUseSetFuncInGetMethodRule extends FlightCustomizationRule {

    private static final PropertyDescriptor<List<String>> PROBLEM_START_WITH_METHOD_NAME_DESCRIPTOR
            = PropertyFactory.stringListProperty("startWithMethodName")
            .desc("this is reports the method name start with literals need handle")
            .defaultValue(Lists.newArrayList("get"))
            .build();

    private static final PropertyDescriptor<List<String>> PROBLEM_FORBIDDEN_START_WITH_FUNC_NAME_DESCRIPTOR
            = PropertyFactory.stringListProperty("forbiddenStartWithFuncName")
            .desc("this is reports the function start with literals need forbidden")
            .defaultValue(Lists.newArrayList("set"))
            .build();

    private Set<String> lambdaContainForbiddenFuncMethod;


    public AvoidUseSetFuncInGetMethodRule() {
        super();
        definePropertyDescriptor(PROBLEM_START_WITH_METHOD_NAME_DESCRIPTOR);
        definePropertyDescriptor(PROBLEM_FORBIDDEN_START_WITH_FUNC_NAME_DESCRIPTOR);
        lambdaContainForbiddenFuncMethod = new ConcurrentSkipListSet<>();
        addRuleChainVisit(ASTLambdaExpression.class);
        addRuleChainVisit(ASTMethodDeclaration.class);

    }

    @Override
    public Object visit(ASTLambdaExpression node, Object data) {
        if (Objects.isNull(node.getFirstChildOfType(ASTBlock.class))) {
            return data;
        }
        List<ASTBlockStatement> blockStatementList = node.getChild(1).findDescendantsOfType(ASTBlockStatement.class);
        if (isContainForbiddenFunc(blockStatementList)) {
            setLambdaContainForbiddenFuncMethodInfo(node);
        }
        return data;
    }

    @Override
    public Object visit(ASTMethodDeclaration node, Object data) {
        if (isTestClass || isTestMethod) {
            return data;
        }

        if (!isMatchMethodName(node) || Objects.isNull(node.getBlock())) {
            return data;
        }
        List<ASTBlockStatement> blockStatementList = node.getBlock().findDescendantsOfType(ASTBlockStatement.class);
        String forbiddenFuncName = getForbiddenFuncName(node, node.getName());
        boolean isLambdaContainForbiddenFunc = !lambdaContainForbiddenFuncMethod.isEmpty()
                && lambdaContainForbiddenFuncMethod.contains(forbiddenFuncName);
        if (isLambdaContainForbiddenFunc || isContainForbiddenFunc(blockStatementList)) {
            addViolationWithPrecisePosition(data, node, AVOID_USE_SET_FUNC_IN_GET_METHOD_VIOLATION_MSG);
        }
        return node;
    }

    private boolean isContainForbiddenFunc(List<ASTBlockStatement> blockStatementList) {
        String[] imageNames;
        String imageName;
        for (ASTBlockStatement blockStatement : blockStatementList) {
            for (ASTName astName : blockStatement.findDescendantsOfType(ASTName.class)) {
                imageNames = astName.getImage().split("\\.");
                if (imageNames.length > 1 && isMatchForbiddenFunc(imageNames[1])) {
                    return true;
                }
            }
            for (ASTPrimarySuffix astPrimarySuffix : blockStatement.findDescendantsOfType(ASTPrimarySuffix.class)) {
                imageName = astPrimarySuffix.getImage();
                if (StringUtils.isNotEmpty(imageName) && isMatchForbiddenFunc(imageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isMatchForbiddenFunc(String imageName) {
        final List<String> startWithFuncNameList = getProperty(PROBLEM_FORBIDDEN_START_WITH_FUNC_NAME_DESCRIPTOR);
        return startWithFuncNameList.stream()
                .anyMatch(methodName -> StringUtils.equalsIgnoreCase(methodName,
                        getStartWithName(imageName, methodName.length())));
    }

    private boolean isMatchMethodName(ASTMethodDeclaration node) {
        final List<String> startWithMethodNameList = getProperty(PROBLEM_START_WITH_METHOD_NAME_DESCRIPTOR);
        String imageName = node.getName();
        if (startWithMethodNameList.isEmpty()
                || StringUtils.isEmpty(imageName)) {
            return false;
        }

        return startWithMethodNameList.stream()
                .anyMatch(methodName -> StringUtils.equalsIgnoreCase(methodName,
                        getStartWithName(imageName, methodName.length())));
    }

    private String getStartWithName(String imageName, int length) {
        if (imageName.length() < length) {
            return "";
        }
        return imageName.substring(0, length);
    }

    private void setLambdaContainForbiddenFuncMethodInfo(ASTLambdaExpression node) {
        List<ASTMethodDeclaration> methodDeclarationList = node.getParentsOfType(ASTMethodDeclaration.class);
        if (methodDeclarationList.isEmpty()) {
            return;
        }

        for (ASTMethodDeclaration astMethodDeclaration : methodDeclarationList) {
            String forbiddenFuncName = getForbiddenFuncName(node, astMethodDeclaration.getName());
            if (StringUtils.isNotEmpty(forbiddenFuncName)
                    && !lambdaContainForbiddenFuncMethod.contains(forbiddenFuncName)) {
                lambdaContainForbiddenFuncMethod.add(forbiddenFuncName);
            }
        }
    }


    /**
     * 防止不同类中有相同的方法名，所以在匹配lambda表达式中存在set的方法时加上className和methodName一起匹配
     *
     * @param node
     * @param methodName
     * @return
     */
    private String getForbiddenFuncName(JavaNode node, String methodName) {
        String className = Optional.ofNullable(node.getParentsOfType(ASTClassOrInterfaceDeclaration.class))
                .orElse(new ArrayList<>())
                .stream()
                .filter(Objects::nonNull)
                .map(ASTClassOrInterfaceDeclaration::getSimpleName)
                .findFirst()
                .orElse("");
        if (StringUtils.isEmpty(className)
                || StringUtils.isEmpty(methodName)) {
            return "";
        } else {
            return String.format("%s_%s", className, methodName);
        }
    }


    private static final String AVOID_USE_SET_FUNC_IN_GET_METHOD_VIOLATION_MSG =
            "java.customization.AvoidUseSetFuncInGetMethodRule.violation.msg";
}
