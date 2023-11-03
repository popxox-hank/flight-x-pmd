package com.ctrip.flight.mobile.pmd.lang.java.rule;

import com.google.common.collect.Lists;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.lang.java.types.TypeTestUtil;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author haoren
 * Create at: 2023-08-25
 */
public class FlightStreamExpressionRule extends FlightCustomizationRule {

    private static final String STREAM_CONSTANT = "stream";
    private static final String OPTIONAL_CONSTANT = "Optional";
    private static final String MONO_CONSTANT = "Mono";
    private static final String FLUX_CONSTANT = "Flux";
    private static final List<String> UN_CHECK_SUFFIX_METHOD_NAME = Lists.newArrayList("get", "isPresent");
    private List<String> parameterStreamVariableList;
    private List<String> methodStreamVariableList;
    private List<String> localStreamVariableList;
    protected List<StreamInfo> streamInfoList;


    public FlightStreamExpressionRule() {
        super();
        parameterStreamVariableList = new ArrayList<>();
        methodStreamVariableList = new ArrayList<>();
        localStreamVariableList = new ArrayList<>();
        addRuleChainVisit(ASTFormalParameter.class);
        addRuleChainVisit(ASTMethodDeclaration.class);
    }


    @Override
    public Object visit(ASTMethodDeclaration node, Object data) {
        if (node.getNumChildren() < 2) {
            return data;
        }
        ASTType astType = node.getChild(0).getFirstChildOfType(ASTType.class);
        if (Objects.isNull(astType)) {
            return null;
        }

        if (isStreamExpressionType(astType)
                && node.getChild(1) instanceof ASTMethodDeclarator
                && StringUtils.isNotEmpty(node.getChild(1).getImage())) {
            methodStreamVariableList.add(getStreamVariableName(node, node.getChild(1).getImage()));
        }
        return data;
    }


    @Override
    public Object visit(ASTFormalParameter node, Object data) {
        if (node.getNumChildren() == 2
                && node.getChild(0) instanceof ASTType
                && isStreamExpressionType((ASTType) node.getChild(0))) {
            parameterStreamVariableList.add(getStreamVariableName(node, node.getChild(1).getImage()));
        }
        return data;
    }


    protected boolean isStreamExpressionAndSetStreamInfo(ASTPrimaryExpression node) {
        String imageName;
        streamInfoList = new ArrayList<>();
        for (int i = 0; i < node.getNumChildren(); i++) {
            imageName = getPrimaryExpressionImageName(node.getChild(i));
            if (isStream(node, imageName)) {
                streamInfoList.add(getStreamInfo(imageName, i));
                return true;
            }
        }
        return false;
    }


    /**
     * 是否是stream表达式
     *
     * @param node
     * @param isOnlyCheckStreamName:是否只通过name来匹配是stream表达式
     * @return
     */
    protected boolean isStreamExpression(ASTPrimaryExpression node, boolean isOnlyCheckStreamName) {
        if (isSpecialStreamExpression(node)) {
            return false;
        }
        String imageName;
        for (int i = 0; i < node.getNumChildren(); i++) {
            imageName = getPrimaryExpressionImageName(node.getChild(i));
            if (isStream(node, imageName) && !isOnlyCheckStreamName) {
                return true;
            }
            if (isStreamName(imageName) && isOnlyCheckStreamName) {
                return true;
            }
        }
        return false;
    }

    /**
     * 通过方法或者内部变量获取的流表达式，可能因为部分节点名称和方法或内部变量名称相同而造成被判断为流表达式，需要特殊处理一下
     *
     * @param node
     * @return
     */
    protected boolean isSpecialStreamExpression(JavaNode node) {
        // 两层父节点是ASTArgumentList
        boolean isParentArgument = Objects.nonNull(node)
                && Objects.nonNull(node.getParent())
                && Objects.nonNull(node.getParent().getParent())
                && node.getParent().getParent() instanceof ASTArgumentList;
        // 父节点是ASTEqualityExpression
        boolean isParentEquality = Objects.nonNull(node)
                && Objects.nonNull(node.getParent())
                && node.getParent() instanceof ASTEqualityExpression;
        return isParentArgument || isParentEquality;
    }


    protected String getPrimaryExpressionImageName(JavaNode node) {
        return (node instanceof ASTPrimaryPrefix && node.getNumChildren() > 0)
                ? Optional.ofNullable(node.getChild(0).getImage()).orElse("")
                : Optional.ofNullable(node.getImage()).orElse("");
    }

    /**
     * 判断当前的内部变量是否是stream表达式
     *
     * @param imageName
     * @return
     */
    protected boolean isStreamVariable(JavaNode javaNode, String imageName) {
        List<String> imageList = getStreamImageNameList(javaNode, imageName);
        if (imageList.isEmpty()
                || Objects.isNull(parameterStreamVariableList)
                || Objects.isNull(methodStreamVariableList)
                || Objects.isNull(localStreamVariableList)) {
            return false;
        }

        return parameterStreamVariableList.stream()
                .anyMatch(streamVariable -> matchStreamName(imageList, streamVariable))
                || methodStreamVariableList.stream()
                .anyMatch(streamVariable -> matchStreamName(imageList, streamVariable))
                || localStreamVariableList.stream()
                .anyMatch(streamVariable -> matchStreamName(imageList, streamVariable));
    }

    /**
     * 从method获取的stream比较特殊，后续跟着的.xxx的流节点不在prefix节点中，所以需要判断出来剔除
     *
     * @param node
     * @return
     */
    protected boolean isStreamMethodVariable(JavaNode node) {
        if (!(node instanceof ASTPrimaryExpression)) {
            return false;
        }
        String imageName;
        for (int i = 0; i < node.getNumChildren(); i++) {
            imageName = getPrimaryExpressionImageName(node.getChild(i));
            if (isStreamMethodVariable(node, imageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * AST语法树的imageName是否是流表达式
     *
     * @param imageName
     * @return
     */
    protected boolean isStreamName(String imageName) {
        List<String> imageList = getImageNameList(imageName);
        if (imageList.isEmpty()) {
            return false;
        }
        return matchStreamName(imageList, STREAM_CONSTANT)
                || matchStreamName(imageList, OPTIONAL_CONSTANT)
                || matchStreamName(imageList, MONO_CONSTANT)
                || matchStreamName(imageList, FLUX_CONSTANT);
    }

    protected boolean isStream(JavaNode javaNode, String imageName) {
        return isStreamName(imageName) || isStreamVariable(javaNode, imageName);
    }

    protected List<String> getStreamImageNameList(JavaNode javaNode, String imageName) {
        if (StringUtils.isEmpty(imageName)) {
            return new ArrayList<>();
        }
        List<String> imageList = Arrays.asList(imageName.split("\\."));
        if (imageList.isEmpty()) {
            return new ArrayList<>();
        }
        return imageList.stream()
                .filter(Objects::nonNull)
                .map(name -> getStreamVariableName(javaNode, name))
                .collect(Collectors.toList());
    }

    protected List<String> getImageNameList(String imageName) {
        if (StringUtils.isEmpty(imageName)) {
            return new ArrayList<>();
        }
        List<String> imageList = Arrays.asList(imageName.split("\\."));
        if (imageList.isEmpty()) {
            return new ArrayList<>();
        }
        return imageList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 设置localVariable是stream的变量名称
     *
     * @param node
     */
    protected void setLocalStreamVariableName(ASTPrimaryExpression node) {
        List<ASTVariableDeclaratorId> variableDeclaratorIdList;
        for (ASTVariableDeclarator variableDeclarator : node.getParentsOfType(ASTVariableDeclarator.class)) {
            variableDeclaratorIdList = variableDeclarator.findDescendantsOfType(ASTVariableDeclaratorId.class);
            for (ASTVariableDeclaratorId variableDeclaratorId : variableDeclaratorIdList) {
                if (StringUtils.isNotEmpty(variableDeclaratorId.getImage())
                        && isStreamExpressionDeclaratorNode(variableDeclaratorId)) {
                    localStreamVariableList.add(getStreamVariableName(node, variableDeclaratorId.getImage()));
                }
            }
        }
    }


    /**
     * 对于Optional对象的get或者isPresent方法特殊处理，不需要检测
     *
     * @param expressionNode
     * @return
     */
    protected boolean unCheckViolation(JavaNode expressionNode) {
        List<String> prefixImageNameList = getPrefixImageName(expressionNode);
        List<String> suffixImageNameList = getSuffixImageName(expressionNode);
        if (prefixImageNameList.isEmpty() || suffixImageNameList.size() > 2) {
            return false;
        }

        boolean isUnCheckPrefix;
        boolean isUnCheckSuffix;
        if (suffixImageNameList.size() < 2) {
            isUnCheckPrefix = prefixImageNameList.stream()
                    .anyMatch(imageName -> StringUtils.equals(imageName, OPTIONAL_CONSTANT)
                            || isStreamVariable(expressionNode, imageName));
            isUnCheckSuffix = prefixImageNameList.stream()
                    .anyMatch(UN_CHECK_SUFFIX_METHOD_NAME::contains);
        } else {
            isUnCheckPrefix = prefixImageNameList.stream()
                    .anyMatch(imageName -> StringUtils.equals(imageName, OPTIONAL_CONSTANT)
                            || isStreamVariable(expressionNode, imageName));
            isUnCheckSuffix = suffixImageNameList.stream()
                    .anyMatch(UN_CHECK_SUFFIX_METHOD_NAME::contains);
        }


        return isUnCheckPrefix && isUnCheckSuffix;
    }


    private StreamInfo getStreamInfo(String imageName, int index) {
        StreamInfo streamInfo = new StreamInfo();
        streamInfo.setImageName(imageName);
        streamInfo.setStreamIndex(index);
        return streamInfo;
    }

    /**
     * 从method获取的stream比较特殊，后续跟着的.xxx的流节点不在prefix节点中，所以需要判断出来剔除
     *
     * @param imageName
     * @return
     */
    private boolean isStreamMethodVariable(JavaNode javaNode, String imageName) {
        List<String> imageList = getStreamImageNameList(javaNode, imageName);
        if (imageList.isEmpty()
                || Objects.isNull(methodStreamVariableList)) {
            return false;
        }
        return methodStreamVariableList.stream()
                .anyMatch(streamVariable -> matchStreamName(imageList, streamVariable));
    }


    private boolean matchStreamName(List<String> imageList, String streamName) {
        return imageList.stream().anyMatch(x -> StringUtils.equals(x, streamName));
    }

    /**
     * 获取各节点中的流表达式的名称
     *
     * @param expressionNode
     * @return
     */
    private List<String> getSuffixImageName(JavaNode expressionNode) {
        if (!(expressionNode instanceof ASTPrimaryExpression)) {
            return Collections.emptyList();
        }

        ASTPrimaryExpression astPrimaryExpression = (ASTPrimaryExpression) expressionNode;
        List<String> imageNameList = new ArrayList<>();

        List<ASTPrimarySuffix> suffixes = astPrimaryExpression.findChildrenOfType(ASTPrimarySuffix.class);
        for (ASTPrimarySuffix suffix : suffixes) {
            if (!suffix.isArguments() && !suffix.isArrayDereference() && StringUtils.isNotEmpty(suffix.getImage())) {
                imageNameList.add(suffix.getImage());
            }
        }

        return imageNameList;
    }

    private List<String> getPrefixImageName(JavaNode expressionNode) {
        if (!(expressionNode instanceof ASTPrimaryExpression)) {
            return Collections.emptyList();
        }

        ASTPrimaryExpression astPrimaryExpression = (ASTPrimaryExpression) expressionNode;
        List<String> imageNameList = new ArrayList<>();

        ASTPrimaryPrefix astPrimaryPrefix = astPrimaryExpression.getFirstChildOfType(ASTPrimaryPrefix.class);
        if (Objects.isNull(astPrimaryPrefix)) {
            return imageNameList;
        }

        ASTName astName = astPrimaryPrefix.getFirstChildOfType(ASTName.class);
        if (Objects.isNull(astName)) {
            return imageNameList;
        }
        return getImageNameList(astName.getImage());
    }

    /**
     * 判断内部变量是否是stream表达式
     *
     * @param variableDeclaratorId
     * @return
     */
    private boolean isStreamExpressionDeclaratorNode(ASTVariableDeclaratorId variableDeclaratorId) {
        return TypeTestUtil.isA(Stream.class, variableDeclaratorId)
                || TypeTestUtil.isA(Optional.class, variableDeclaratorId)
                || TypeTestUtil.isA(Mono.class, variableDeclaratorId)
                || TypeTestUtil.isA(Flux.class, variableDeclaratorId);
    }

    /**
     * 判断method返回类型和入参是否是stream表达式
     *
     * @param astType
     * @return
     */
    private boolean isStreamExpressionType(ASTType astType) {
        return TypeTestUtil.isA(Stream.class, astType)
                || TypeTestUtil.isA(Optional.class, astType)
                || TypeTestUtil.isA(Mono.class, astType)
                || TypeTestUtil.isA(Flux.class, astType);
    }

    /**
     * 对于是从方法、参数或者本地变量获取的流信息增加类名
     *
     * @param node
     * @param originName
     * @return
     */
    private String getStreamVariableName(JavaNode node, String originName) {
        if (Objects.isNull(node)) {
            return originName;
        }
        List<ASTClassOrInterfaceDeclaration> declarationList =
                node.getParentsOfType(ASTClassOrInterfaceDeclaration.class);
        if (declarationList.isEmpty()) {
            return originName;
        }
        String className = declarationList.get(0).getSimpleName();
        if (StringUtils.isEmpty(className)) {
            return originName;
        }

        return String.format("%s_%s", className, originName);
    }

    protected class StreamInfo {
        private String imageName;
        private Integer streamIndex;

        public String getImageName() {
            return imageName;
        }

        void setImageName(String imageName) {
            this.imageName = imageName;
        }

        public Integer getStreamIndex() {
            return streamIndex;
        }

        void setStreamIndex(Integer streamIndex) {
            this.streamIndex = streamIndex;
        }
    }


}
