package com.ctrip.flight.mobile.pmd.lang.java.rule.customization;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightCustomizationRule;
import com.google.common.collect.ImmutableSet;
import com.google.gson.internal.LinkedHashTreeMap;
import com.google.gson.internal.LinkedTreeMap;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.lang.java.types.TypeTestUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author haoren
 * Create at: 2023-09-28
 */
public class AvoidOperateExternalVariInStreamForeachStmtsRule extends FlightCustomizationRule {

    private Set<String> externalVariableSet;
    private Set<String> streamVariableSet;
    private static final String FORBIDDEN_FUNC = "forEach";
    private static final String SPECIAL_OPERATE = "collect";
    private static final String STREAM_FUNC = "stream";
    private static final Set<String> FORBIDDEN_OPERATE = ImmutableSet.of("add", "addAll", "put", "putAll", "remove",
            "removeAll", "merge", "removeRange", "set", "retainAll", "subList", "replaceAll", "addFirst", "addLast",
            "offer", "offerFirst", "offerLast", "push", "pop");
    private static final Set<String> SPECIAL_FORBIDDEN_OPERATE = ImmutableSet.of("set");

    public AvoidOperateExternalVariInStreamForeachStmtsRule() {
        super();
        externalVariableSet = new HashSet<>();
        streamVariableSet = new HashSet<>();
        // 先确认哪些是stream的表达式并带有foreach语句，然后再确认哪些是外部变量Map、List和包含set方法
        addRuleChainVisit(ASTMethodDeclaration.class);
        addRuleChainVisit(ASTFieldDeclaration.class);
        addRuleChainVisit(ASTFormalParameter.class);
        addRuleChainVisit(ASTLambdaExpression.class);
    }


    @Override
    public Object visit(ASTFieldDeclaration node, Object data) {
        externalVariableSet.addAll(Optional.ofNullable(node.getFirstDescendantOfType(ASTVariableDeclarator.class))
                .map(variableDeclarator -> variableDeclarator.findChildrenOfType(ASTVariableDeclaratorId.class))
                .orElse(new ArrayList<>())
                .stream()
                .filter(Objects::nonNull)
                .filter(variableDeclaratorId -> isMap(variableDeclaratorId)
                        || isList(variableDeclaratorId)
                        || isSet(variableDeclaratorId))
                .map(ASTVariableDeclaratorId::getName)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.toSet()));
        streamVariableSet.addAll(Optional.ofNullable(node.getFirstDescendantOfType(ASTVariableDeclarator.class))
                .map(variableDeclarator -> variableDeclarator.findChildrenOfType(ASTVariableDeclaratorId.class))
                .orElse(new ArrayList<>())
                .stream()
                .filter(Objects::nonNull)
                .filter(this::isStreamExpression)
                .map(ASTVariableDeclaratorId::getName)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.toSet()));
        return data;
    }

    @Override
    public Object visit(ASTFormalParameter node, Object data) {
        externalVariableSet.addAll(node.findChildrenOfType(ASTVariableDeclaratorId.class)
                .stream()
                .filter(Objects::nonNull)
                .filter(variableDeclaratorId -> isMap(variableDeclaratorId)
                        || isList(variableDeclaratorId)
                        || isSet(variableDeclaratorId))
                .map(ASTVariableDeclaratorId::getName)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.toSet()));
        streamVariableSet.addAll(node.findChildrenOfType(ASTVariableDeclaratorId.class)
                .stream()
                .filter(Objects::nonNull)
                .filter(this::isStreamExpression)
                .map(ASTVariableDeclaratorId::getName)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.toSet()));
        return data;
    }


    /**
     * 根据stream表达式确认该lambda表达式是否在foreach语句中的
     *
     * @param node
     * @param data
     * @return
     */
    @Override
    public Object visit(ASTLambdaExpression node, Object data) {
        if (isTestClass || isTestMethod) {
            return data;
        }

        List<ASTStatementExpression> statementExpressionList = node.getParentsOfType(ASTStatementExpression.class);
        if (statementExpressionList.isEmpty()
                || !isContainForeach(statementExpressionList)
                || !isContainStream(statementExpressionList)) {
            return data;
        }
        if (isChangeExternalVariable(node)) {
            addViolationWithPrecisePosition(data, node,
                    AVOID_OPERATE_EXTERNAL_VARI_IN_STREAM_FOREACH_STMTS_VIOLATION_MSG);
        }

        return data;
    }


    @Override
    public Object visit(ASTMethodDeclaration node, Object data) {
        List<ASTVariableDeclaratorId> variableDeclaratorIdList =
                node.findDescendantsOfType(ASTLocalVariableDeclaration.class)
                        .stream()
                        .filter(Objects::nonNull)
                        .map(localVariable -> localVariable.findChildrenOfType(ASTVariableDeclarator.class))
                        .flatMap(List::stream)
                        .filter(Objects::nonNull)
                        .map(variableDeclarator -> variableDeclarator.findChildrenOfType(ASTVariableDeclaratorId.class))
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
        externalVariableSet.addAll(variableDeclaratorIdList.stream()
                .filter(variableDeclarator -> isMap(variableDeclarator)
                        || isList(variableDeclarator)
                        || isSet(variableDeclarator))
                .map(ASTVariableDeclaratorId::getName)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.toSet()));
        // 内部变量是Stream
        streamVariableSet.addAll(variableDeclaratorIdList.stream()
                .filter(this::isStreamExpression)
                .map(ASTVariableDeclaratorId::getName)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.toSet()));
        // method返回是Stream
        if (isMethodReturnStream(node)) {
            streamVariableSet.add(node.getName());
        }
        return data;
    }

    /**
     * 判断方法的返回类型是否Stream
     *
     * @param node
     * @return
     */
    private boolean isMethodReturnStream(ASTMethodDeclaration node) {
        ASTResultType astResultType = node.getFirstChildOfType(ASTResultType.class);
        return Objects.nonNull(astResultType)
                && astResultType.getNumChildren() > 0
                && astResultType.getChild(0) instanceof ASTType
                && isStreamExpression((ASTType) astResultType.getChild(0));
    }

    /**
     * 判断foreach的lambda表达式中是否存在变更外部变量
     *
     * @param node
     * @return
     */
    private boolean isChangeExternalVariable(ASTLambdaExpression node) {
        for (ASTBlockStatement blockStatement : node.findDescendantsOfType(ASTBlockStatement.class)) {
            for (ASTPrimaryExpression primaryExpression :
                    blockStatement.findDescendantsOfType(ASTPrimaryExpression.class)) {
                if (isChangeExternalVariable(primaryExpression)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isChangeExternalVariable(ASTPrimaryExpression primaryExpression) {
        if (Objects.isNull(primaryExpression)
                || primaryExpression.getNumChildren() == 0) {
            return false;
        }
        List<String[]> prefixImageNameList = primaryExpression.findChildrenOfType(ASTPrimaryPrefix.class)
                .stream()
                .filter(Objects::nonNull)
                .map(this::getImageNames)
                .filter(x -> x.length > 0)
                .collect(Collectors.toList());
        Set<String> suffixImageNameSet = primaryExpression.findChildrenOfType(ASTPrimarySuffix.class)
                .stream()
                .filter(Objects::nonNull)
                .map(ASTPrimarySuffix::getImage)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.toSet());
        return isExistsForbiddenOperate(prefixImageNameList, suffixImageNameSet);
    }

    /**
     * 判断foreach的lambda表达式中是否存在禁止的操作
     *
     * @param prefixImageNameList
     * @param suffixImageNameSet
     * @return
     */
    private boolean isExistsForbiddenOperate(List<String[]> prefixImageNameList,
                                             Set<String> suffixImageNameSet) {
        if (prefixImageNameList.isEmpty() && suffixImageNameSet.isEmpty()) {
            return false;
        }

        boolean isPrefixExistsForbiddenOperate = prefixImageNameList.stream()
                .anyMatch(prefixImageName -> prefixImageName.length == 2
                        && isMatchForbiddenOperate(prefixImageName));
        return isPrefixExistsForbiddenOperate || isMatchSpecialOperate(suffixImageNameSet);

    }

    /**
     * 是否是禁止的操作或者model的set操作
     *
     * @param prefixImageName
     * @return
     */
    private boolean isMatchForbiddenOperate(String[] prefixImageName) {
        boolean isForbiddenOperate = externalVariableSet.contains(prefixImageName[0])
                && FORBIDDEN_OPERATE.contains(prefixImageName[1]);
        return isForbiddenOperate || isMatchSpecialOperate(ImmutableSet.of(prefixImageName[1]));
    }

    /**
     * 对model的set方法做处理
     *
     * @param suffixImageNameSet
     * @return
     */
    private boolean isMatchSpecialOperate(Set<String> suffixImageNameSet) {
        for (String forbiddenOperateName : SPECIAL_FORBIDDEN_OPERATE) {
            boolean isExistsSpecial = suffixImageNameSet
                    .stream()
                    .filter(Objects::nonNull)
                    .filter(imageName -> imageName.length() >= forbiddenOperateName.length())
                    .map(imageName -> imageName.substring(0, forbiddenOperateName.length()))
                    .anyMatch(x -> StringUtils.equalsIgnoreCase(forbiddenOperateName, x));
            if (isExistsSpecial) {
                return true;
            }
        }
        return false;
    }

    /**
     * 或者lambda表达式中的变量名
     *
     * @param node
     * @return
     */
    private String[] getImageNames(JavaNode node) {
        ASTName astName = Optional.ofNullable(node)
                .map(x -> x.getFirstChildOfType(ASTName.class))
                .orElse(null);
        if (Objects.isNull(astName)) {
            return new String[]{};
        }
        return astName.getImage().split("\\.");
    }

    /**
     * 判断是否含有forEach语句
     *
     * @param statementExpressionList
     * @return
     */
    private boolean isContainForeach(List<ASTStatementExpression> statementExpressionList) {
        return statementExpressionList.stream()
                .filter(Objects::nonNull)
                .map(statementExpression -> statementExpression.findDescendantsOfType(ASTPrimarySuffix.class))
                .flatMap(List::stream)
                .anyMatch(suffix -> StringUtils.equalsIgnoreCase(suffix.getImage(), FORBIDDEN_FUNC));
    }


    /**
     * 判断是否未包含stream
     *
     * @param statementExpressionList
     * @return
     */
    private boolean isContainStream(List<ASTStatementExpression> statementExpressionList) {
        // 后缀中是否包含有stream关键字
        boolean isSuffixContainStream = statementExpressionList.stream()
                .filter(Objects::nonNull)
                .map(statementExpression -> statementExpression.findDescendantsOfType(ASTPrimarySuffix.class))
                .flatMap(List::stream)
                .anyMatch(suffix -> StringUtils.equalsIgnoreCase(suffix.getImage(), STREAM_FUNC));
        return isSuffixContainStream || isPrefixContainStream(statementExpressionList);
    }

    /**
     * 前缀中是否包含有stream的变量
     *
     * @param statementExpressionList
     * @return
     */
    private boolean isPrefixContainStream(List<ASTStatementExpression> statementExpressionList) {
        boolean isStream;
        boolean isStreamVariable;
        boolean isUnContainCollect;
        ASTPrimaryExpression astPrimaryExpression;
        List<ASTPrimaryPrefix> astPrimaryPrefixList;
        for (ASTStatementExpression astStatementExpression : statementExpressionList) {
            astPrimaryExpression = astStatementExpression.getFirstChildOfType(ASTPrimaryExpression.class);
            if (Objects.isNull(astPrimaryExpression)) {
                continue;
            }
            astPrimaryPrefixList = astPrimaryExpression.findChildrenOfType(ASTPrimaryPrefix.class);
            isUnContainCollect = astPrimaryExpression.findChildrenOfType(ASTPrimarySuffix.class)
                    .stream()
                    .filter(Objects::nonNull)
                    .noneMatch(suffix -> StringUtils.equalsIgnoreCase(suffix.getImage(), SPECIAL_OPERATE));
            isStream = isStream(astPrimaryPrefixList);
            isStreamVariable = isStreamVariable(astPrimaryPrefixList);
            // 特殊场景stream.collect(Collectors.toList()).forEach
            if ((isStream && isUnContainCollect) || (isStreamVariable && isUnContainCollect)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 是否包含带有stream关键字的stream表达式
     *
     * @param astPrimaryPrefixList
     * @return
     */
    private boolean isStream(List<ASTPrimaryPrefix> astPrimaryPrefixList) {
        for (ASTPrimaryPrefix astPrimaryPrefix : astPrimaryPrefixList) {
            String[] imageNames = getImageNames(astPrimaryPrefix);
            if (imageNames.length == 2
                    && StringUtils.equalsIgnoreCase(imageNames[1], STREAM_FUNC)) {
                return true;
            }
        }
        return false;
    }

    private boolean isStreamVariable(List<ASTPrimaryPrefix> astPrimaryPrefixList) {
        for (ASTPrimaryPrefix astPrimaryPrefix : astPrimaryPrefixList) {
            String[] imageNames = getImageNames(astPrimaryPrefix);
            if (imageNames.length == 1
                    && streamVariableSet.contains(imageNames[0])) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断字段、内部变量和参数是否是map类型
     *
     * @param typeNode
     * @return
     */
    private boolean isMap(TypeNode typeNode) {
        if (Objects.isNull(typeNode)) {
            return false;
        }

        return TypeTestUtil.isA(Map.class, typeNode)
                || TypeTestUtil.isA(HashMap.class, typeNode)
                || TypeTestUtil.isA(ConcurrentHashMap.class, typeNode)
                || TypeTestUtil.isA(LinkedHashMap.class, typeNode)
                || TypeTestUtil.isA(LinkedHashTreeMap.class, typeNode)
                || TypeTestUtil.isA(LinkedTreeMap.class, typeNode)
                || TypeTestUtil.isA(AbstractMap.class, typeNode)
                || TypeTestUtil.isA(SortedMap.class, typeNode)
                || TypeTestUtil.isA(TreeMap.class, typeNode)
                || TypeTestUtil.isA(Hashtable.class, typeNode);
    }

    /**
     * 判断字段、内部变量和参数是否是List类型
     *
     * @param typeNode
     * @return
     */
    private boolean isList(TypeNode typeNode) {
        if (Objects.isNull(typeNode)) {
            return false;
        }

        return TypeTestUtil.isA(List.class, typeNode)
                || TypeTestUtil.isA(ArrayList.class, typeNode)
                || TypeTestUtil.isA(LinkedList.class, typeNode)
                || TypeTestUtil.isA(AbstractList.class, typeNode);
    }

    /**
     * 判断字段、内部变量和参数是否是Set类型
     *
     * @param typeNode
     * @return
     */
    private boolean isSet(TypeNode typeNode) {
        if (Objects.isNull(typeNode)) {
            return false;
        }
        return TypeTestUtil.isA(Set.class, typeNode)
                || TypeTestUtil.isA(HashSet.class, typeNode)
                || TypeTestUtil.isA(SortedSet.class, typeNode)
                || TypeTestUtil.isA(TreeSet.class, typeNode)
                || TypeTestUtil.isA(AbstractSet.class, typeNode);
    }

    /**
     * 判断字段、内部变量和参数是否是Stream类型
     *
     * @param typeNode
     * @return
     */
    private boolean isStreamExpression(TypeNode typeNode) {
        return Objects.nonNull(typeNode)
                && TypeTestUtil.isA(Stream.class, typeNode);
    }

    private static final String AVOID_OPERATE_EXTERNAL_VARI_IN_STREAM_FOREACH_STMTS_VIOLATION_MSG =
            "java.customization.AvoidOperateExternalVariInStreamForeachStmtsRule.violation.msg";

}