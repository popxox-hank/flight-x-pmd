package com.ctrip.flight.mobile.pmd.lang.java.rule.customization;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightJavaRule;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.lang.java.types.TypeTestUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author haoren
 * Create at: 2023-09-20
 */
public class AvoidGetEnumUseForStatementRule extends FlightJavaRule {

    private static final String ARRAYS_STREAM = "Arrays.stream";
    private static final String ENUM_VALUES = "values";

    @Override
    public Object visit(ASTMethodDeclaration node, Object data) {
        if (isEnumClass(node) && isReturnEnumType(node) && isUseForStatement(node)) {
            addViolationWithPrecisePosition(data, node, AVOID_GET_ENUM_USE_FOR_STATEMENT_VIOLATION_MSG);
        }
        return data;
    }

    /**
     * 判断方法所在类是否是一个枚举
     *
     * @param node
     * @return
     */
    private boolean isEnumClass(ASTMethodDeclaration node) {
        return Objects.nonNull(node.getParent())
                && Objects.nonNull(node.getParent().getParent())
                && node.getParent().getParent().getParent() instanceof ASTEnumDeclaration;
    }

    /**
     * 判断该方法是否返回枚举
     *
     * @param node
     * @return
     */
    private boolean isReturnEnumType(ASTMethodDeclaration node) {
        ASTClassOrInterfaceType classOrInterfaceType =
                node.getResultType().getFirstDescendantOfType(ASTClassOrInterfaceType.class);
        String enumImageName = getEnumClassImageName(node);

        return isMatchEnumType(enumImageName, classOrInterfaceType);
    }

    /**
     * 判断是否是使用O(n)复杂度的循环方式来比对获取枚举
     * 一个是用for语句，一个是用Arrays.stream通过表达式来获取
     *
     * @param node
     * @return
     */
    private boolean isUseForStatement(ASTMethodDeclaration node) {
        List<ASTForStatement> forStatementList = node.getBlock().findDescendantsOfType(ASTForStatement.class);
        List<ASTPrimaryExpression> primaryExpressionList =
                node.getBlock().findDescendantsOfType(ASTPrimaryExpression.class);
        if (forStatementList.isEmpty()
                && primaryExpressionList.isEmpty()) {
            return false;
        }

        return isLocalVariableEnumType(node, forStatementList)
                || isUseArraysStreamAndEnumValues(primaryExpressionList);
    }

    private boolean isUseArraysStreamAndEnumValues(List<ASTPrimaryExpression> primaryExpressionList) {
        return isUseArraysStream(primaryExpressionList) && isUseEnumValues(primaryExpressionList);
    }

    /**
     * 判断Arrays.Stream是否转换的枚举的values
     *
     * @param primaryExpressionList
     * @return
     */
    private boolean isUseEnumValues(List<ASTPrimaryExpression> primaryExpressionList) {
        for (ASTPrimaryExpression astPrimaryExpression : primaryExpressionList) {
            List<ASTName> astNameList = astPrimaryExpression.findDescendantsOfType(ASTName.class);
            if (isMatchEnumValues(astNameList)) {
                return true;
            }
        }
        return false;
    }

    private boolean isMatchEnumValues(List<ASTName> astNameList) {
        return Optional.ofNullable(astNameList)
                       .orElse(new ArrayList<>())
                       .stream()
                       .filter(Objects::nonNull)
                       .anyMatch(astName -> StringUtils.equalsIgnoreCase(
                               getEnumValuesImageName(astName.getImage()), ENUM_VALUES));
    }

    private String getEnumValuesImageName(String imageName) {
        String[] imageNames = imageName.split("\\.");
        if (imageNames.length > 1) {
            return imageNames[1];
        }
        return "";
    }

    /**
     * 判断是否使用Arrays.Stream
     *
     * @param primaryExpressionList
     * @return
     */
    private boolean isUseArraysStream(List<ASTPrimaryExpression> primaryExpressionList) {
        for (ASTPrimaryExpression astPrimaryExpression : primaryExpressionList) {
            List<ASTName> astNameList = astPrimaryExpression.findDescendantsOfType(ASTName.class);
            if (isMatchArraysStream(astNameList)) {
                return true;
            }
        }
        return false;
    }

    private boolean isMatchArraysStream(List<ASTName> astNameList) {
        return Optional.ofNullable(astNameList)
                       .orElse(new ArrayList<>())
                       .stream()
                       .filter(Objects::nonNull)
                       .anyMatch(astName -> StringUtils.equalsIgnoreCase(astName.getImage(), ARRAYS_STREAM));
    }

    /**
     * for语句的内部变量是否是枚举
     *
     * @param node
     * @param forStatementList
     * @return
     */
    private boolean isLocalVariableEnumType(ASTMethodDeclaration node, List<ASTForStatement> forStatementList) {
        ASTClassOrInterfaceType classOrInterfaceType;
        String enumImageName;
        for (ASTForStatement astForStatement : forStatementList) {
            if (astForStatement.isForeach()) {
                classOrInterfaceType =
                        astForStatement.getChild(0).getFirstDescendantOfType(ASTClassOrInterfaceType.class);
                enumImageName = getEnumClassImageName(node);
                if (isMatchEnumType(enumImageName, classOrInterfaceType)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取该枚举类的名称
     *
     * @param node
     * @return
     */
    private String getEnumClassImageName(ASTMethodDeclaration node) {
        List<ASTEnumDeclaration> enumDeclarationList = node.getParentsOfType(ASTEnumDeclaration.class);
        if (enumDeclarationList.isEmpty()) {
            return "";
        }
        return enumDeclarationList.get(0).getImage();
    }

    /**
     * 根据枚举类的名称判断clazz的类型是否是该枚举
     *
     * @param enumImageName
     * @param classOrInterfaceType
     * @return
     */
    private boolean isMatchEnumType(String enumImageName, ASTClassOrInterfaceType classOrInterfaceType) {
        if (StringUtils.isEmpty(enumImageName)
                || Objects.isNull(classOrInterfaceType)) {
            return false;
        }
        return TypeTestUtil.isA(enumImageName, classOrInterfaceType);
    }

    private static final String AVOID_GET_ENUM_USE_FOR_STATEMENT_VIOLATION_MSG =
            "java.customization.AvoidGetEnumUseForStatementRule.violation.msg";
}
