package com.ctrip.flight.mobile.pmd.lang.java.rule.customization;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightCustomizationRule;
import com.google.common.collect.Lists;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.lang.java.types.TypeTestUtil;
import net.sourceforge.pmd.lang.symboltable.NameOccurrence;
import net.sourceforge.pmd.lang.symboltable.ScopedNode;
import net.sourceforge.pmd.properties.PropertyDescriptor;
import net.sourceforge.pmd.properties.PropertyFactory;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author haoren
 * Create at: 2023-09-18
 */
public class AvoidUseUnSynchronizedFormatRule extends FlightCustomizationRule {

    private static final PropertyDescriptor<List<String>> PROBLEM_FORBIDDEN_METHOD_NAME_DESCRIPTOR
            = PropertyFactory.stringListProperty("forbiddenMethodName")
            .desc("this is reports forbidden method name in SimpleDateFormat class")
            .defaultValue(Lists.newArrayList("format", "parse"))
            .build();

    public AvoidUseUnSynchronizedFormatRule() {
        super();
        definePropertyDescriptor(PROBLEM_FORBIDDEN_METHOD_NAME_DESCRIPTOR);
        addRuleChainVisit(ASTFieldDeclaration.class);
    }

    @Override
    public Object visit(ASTFieldDeclaration node, Object data) {
        if (isTestClass || isTestMethod) {
            return data;
        }

        if (!node.isStatic()) {
            return data;
        }

        ASTClassOrInterfaceType cit = node.getFirstDescendantOfType(ASTClassOrInterfaceType.class);
        if (!isNeedCheckClass(cit)) {
            return data;
        }

        ASTVariableDeclaratorId var = node.getFirstDescendantOfType(ASTVariableDeclaratorId.class);
        if (Objects.isNull(var)) {
            return data;
        }

        for (NameOccurrence occ : var.getUsages()) {
            if (isMatchMethodName(occ)
                    && isUnSynchronized(occ, var)
                    && isUnSynchronizedMethod(occ)) {
                addViolationWithPrecisePosition(data, node, AVOID_USE_UN_SYNCHRONIZED_FORMAT_VIOLATION_MSG);
            }
        }
        return data;
    }

    private boolean isMatchMethodName(NameOccurrence occ) {
        ScopedNode imageNode = Optional.ofNullable(occ)
                .map(NameOccurrence::getLocation)
                .orElse(null);
        if (Objects.isNull(imageNode)
                || StringUtils.isEmpty(imageNode.getImage())
                || !imageNode.getImage().contains(".")) {
            return false;
        }
        List<String> imageList = Arrays.asList(imageNode.getImage().split("\\."));
        if (imageList.isEmpty() || imageList.size() < 2) {
            return false;
        }

        final List<String> forbiddenMethodNameList = getProperty(PROBLEM_FORBIDDEN_METHOD_NAME_DESCRIPTOR);

        return forbiddenMethodNameList.stream()
                .anyMatch(methodName -> StringUtils.equalsIgnoreCase(methodName, imageList.get(1)));
    }

    /**
     * 没有Synchronized关键字
     *
     * @param occ
     * @param var
     * @return
     */
    private boolean isUnSynchronized(NameOccurrence occ,
                                     ASTVariableDeclaratorId var) {
        if (Objects.isNull(occ) || Objects.isNull(var)) {
            return true;
        }

        ASTSynchronizedStatement syncStatement = occ.getLocation().getFirstParentOfType(ASTSynchronizedStatement.class);
        if (Objects.isNull(syncStatement)) {
            return true;
        }
        ASTExpression expression = syncStatement.getFirstChildOfType(ASTExpression.class);
        if (Objects.isNull(expression)) {
            return true;
        }

        ASTName name = expression.getFirstDescendantOfType(ASTName.class);
        return Objects.isNull(name) || !Objects.equals(name.getImage(), var.getName());
    }

    private boolean isUnSynchronizedMethod(NameOccurrence occ) {
        if (Objects.isNull(occ)) {
            return true;
        }
        ASTMethodDeclaration method = occ.getLocation().getFirstParentOfType(ASTMethodDeclaration.class);
        return Objects.isNull(method) || !method.isSynchronized() || !method.isStatic();
    }

    private boolean isNeedCheckClass(ASTClassOrInterfaceType cit) {
        return TypeTestUtil.isA(SimpleDateFormat.class, cit)
                || TypeTestUtil.isA(MessageFormat.class, cit)
                || TypeTestUtil.isA(DateFormat.class, cit)
                || TypeTestUtil.isA(NumberFormat.class, cit);
    }


    private static final String AVOID_USE_UN_SYNCHRONIZED_FORMAT_VIOLATION_MSG =
            "java.customization.AvoidUseUnSynchronizedFormatRule.violation.msg";
}
