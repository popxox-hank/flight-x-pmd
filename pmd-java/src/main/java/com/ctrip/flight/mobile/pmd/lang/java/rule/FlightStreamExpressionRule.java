package com.ctrip.flight.mobile.pmd.lang.java.rule;

import com.google.common.collect.Lists;
import net.sourceforge.pmd.lang.java.ast.*;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author haoren
 * Create at: 2023-08-25
 */
public class FlightStreamExpressionRule extends FlightJavaRule {

    private static final String STREAM_CONSTANT = "stream";
    private static final String OPTIONAL_CONSTANT = "optional";
    private static final String MONO_CONSTANT = "mono";
    private static final String FLUX_CONSTANT = "flux";
    private static final List<String> UN_CHECK_SUFFIX_METHOD_NAME = Lists.newArrayList("get", "ispresent");
    private List<String> parameterStreamVariableList;
    private List<String> methodStreamVariableList;
    private List<String> localStreamVariableList;
    protected List<StreamInfo> streamInfoList;


    public FlightStreamExpressionRule() {
        parameterStreamVariableList = new ArrayList<>();
        methodStreamVariableList = new ArrayList<>();
        localStreamVariableList = new ArrayList<>();
        streamInfoList = new ArrayList<>();
        addRuleChainVisit(ASTFormalParameter.class);
        addRuleChainVisit(ASTMethodDeclaration.class);
    }


    @Override
    public Object visit(ASTMethodDeclaration node, Object data) {
        if (isStreamExpression(node)
                && node.getNumChildren() > 1
                && node.getChild(1) instanceof ASTMethodDeclarator
                && StringUtils.isNotEmpty(node.getChild(1).getImage())) {
            methodStreamVariableList.add(node.getChild(1).getImage().toLowerCase());
        }
        return data;
    }


    @Override
    public Object visit(ASTFormalParameter node, Object data) {
        if (isStreamExpression(node) && node.getNumChildren() > 1
                && node.getChild(1) instanceof ASTVariableDeclaratorId
                && StringUtils.isNotEmpty(node.getChild(1).getImage())) {
            parameterStreamVariableList.add(node.getChild(1).getImage().toLowerCase());
        }
        return data;
    }


    protected boolean isStreamExpressionAndSetStreamInfo(ASTPrimaryExpression node) {
        String imageName;
        for (int i = 0; i < node.getNumChildren(); i++) {
            imageName = getPrimaryExpressionImageName(node.getChild(i));
            if (isStream(imageName)) {
                streamInfoList.add(getStreamInfo(imageName, i));
                return true;
            }
        }
        return false;
    }

    private StreamInfo getStreamInfo(String imageName, int index) {
        StreamInfo streamInfo = new StreamInfo();
        streamInfo.setImageName(imageName);
        streamInfo.setStreamIndex(index);
        return streamInfo;
    }

    protected boolean isStreamExpression(ASTPrimaryExpression node, boolean isContainStreamExpression) {
        if (isSpecialStreamExpression(node)) {
            return false;
        }
        String imageName;
        for (int i = 0; i < node.getNumChildren(); i++) {
            imageName = getPrimaryExpressionImageName(node.getChild(i));
            if (isStream(imageName) && !isContainStreamExpression) {
                return true;
            }
            if (isStreamName(imageName) && isContainStreamExpression) {
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
        boolean isParentArgument = Objects.nonNull(node)
                && Objects.nonNull(node.getParent())
                && Objects.nonNull(node.getParent().getParent())
                && node.getParent().getParent() instanceof ASTArgumentList;
        boolean isParentEquality = Objects.nonNull(node)
                && Objects.nonNull(node.getParent())
                && node.getParent() instanceof ASTEqualityExpression;
        return isParentArgument || isParentEquality;
    }

    private boolean isStreamExpression(ASTFormalParameter node) {
        if (node.getNumChildren() == 0) {
            return false;
        }

        return loopCheckIsStreamExpression(node.getChild(0));
    }

    private boolean loopCheckIsStreamExpression(JavaNode node) {
        JavaNode javaNode = node;
        boolean isMatchType = javaNode instanceof ASTType || javaNode instanceof ASTResultType;
        if (!isMatchType) {
            return false;
        }
        boolean hasChild = true;
        while (hasChild) {
            if (javaNode.getNumChildren() == 0) {
                hasChild = false;
            }
            if (javaNode instanceof ASTClassOrInterfaceType
                    && isStreamName(javaNode.getImage())) {
                return true;
            }
            if (hasChild) {
                javaNode = javaNode.getChild(0);
            }
        }
        return false;
    }

    private boolean isStreamExpression(ASTMethodDeclaration node) {
        if (node.getNumChildren() == 0) {
            return false;
        }

        return loopCheckIsStreamExpression(node.getChild(0));
    }

    protected String getPrimaryExpressionImageName(JavaNode node) {
        return (node instanceof ASTPrimaryPrefix && node.getNumChildren() > 0)
                ? Optional.ofNullable(node.getChild(0).getImage()).map(String::toLowerCase).orElse("")
                : Optional.ofNullable(node.getImage()).map(String::toLowerCase).orElse("");
    }

    /**
     * 判断当前的内部变量是否是stream表达式
     *
     * @param imageName
     * @return
     */
    protected boolean isStreamVariable(String imageName) {
        List<String> imageList = getStreamImageNameList(imageName);
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
            if (isStreamMethodVariable(imageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 从method获取的stream比较特殊，后续跟着的.xxx的流节点不在prefix节点中，所以需要判断出来剔除
     *
     * @param imageName
     * @return
     */
    private boolean isStreamMethodVariable(String imageName) {
        List<String> imageList = getStreamImageNameList(imageName);
        if (imageList.isEmpty()
                || Objects.isNull(methodStreamVariableList)) {
            return false;
        }
        return methodStreamVariableList.stream()
                .anyMatch(streamVariable -> matchStreamName(imageList, streamVariable));
    }


    private boolean matchStreamName(List<String> imageList, String streamName) {
        return imageList.stream().anyMatch(x -> StringUtils.equalsIgnoreCase(x, streamName));
    }

    /**
     * AST语法树的imageName是否是流表达式
     *
     * @param imageName
     * @return
     */
    protected boolean isStreamName(String imageName) {
        List<String> imageList = getStreamImageNameList(imageName);
        if (imageList.isEmpty()) {
            return false;
        }
        return matchStreamName(imageList, STREAM_CONSTANT)
                || matchStreamName(imageList, OPTIONAL_CONSTANT)
                || matchStreamName(imageList, MONO_CONSTANT)
                || matchStreamName(imageList, FLUX_CONSTANT);
    }

    protected boolean isStream(String imageName) {
        return isStreamName(imageName) || isStreamVariable(imageName);
    }

    protected List<String> getStreamImageNameList(String imageName) {
        if (StringUtils.isEmpty(imageName)) {
            return new ArrayList<>();
        }
        List<String> imageList = Arrays.asList(imageName.split("\\."));
        if (imageList.isEmpty()) {
            return new ArrayList<>();
        }
        return imageList.stream()
                .filter(Objects::nonNull)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }

    /**
     * 设置localVariable是stream的变量名称
     *
     * @param node
     */
    protected void setContainStreamVariableName(ASTPrimaryExpression node) {
        if (!isStreamExpression(node, false)) {
            return;
        }

        boolean hasParent = true;
        JavaNode javaNode = node.getParent();
        while (hasParent) {
            if (javaNode == null) {
                hasParent = false;
            }
            if (javaNode instanceof ASTVariableDeclarator
                    && javaNode.getNumChildren() > 0) {
                for (JavaNode declaratorNode : javaNode.children()) {
                    if (declaratorNode instanceof ASTVariableDeclaratorId
                            && StringUtils.isNotEmpty(declaratorNode.getImage())
                            && isStreamExpressionDeclaratorNode((ASTVariableDeclaratorId) declaratorNode)) {
                        localStreamVariableList.add(declaratorNode.getImage().toLowerCase());
                    }
                }
                hasParent = false;
            }
            if (hasParent) {
                javaNode = javaNode.getParent();
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
                    .anyMatch(imageName -> StringUtils.equalsIgnoreCase(imageName, OPTIONAL_CONSTANT)
                            || isStreamVariable(imageName));
            isUnCheckSuffix = prefixImageNameList.stream()
                    .anyMatch(UN_CHECK_SUFFIX_METHOD_NAME::contains);
        } else {
            isUnCheckPrefix = prefixImageNameList.stream()
                    .anyMatch(imageName -> StringUtils.equalsIgnoreCase(imageName, OPTIONAL_CONSTANT)
                            || isStreamVariable(imageName));
            isUnCheckSuffix = suffixImageNameList.stream()
                    .anyMatch(UN_CHECK_SUFFIX_METHOD_NAME::contains);
        }


        return isUnCheckPrefix && isUnCheckSuffix;
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
                imageNameList.add(suffix.getImage().toLowerCase());
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
        return getStreamImageNameList(astName.getImage());
    }

    /**
     * 有些内部变量是通过stream方法赋值的需要剔除
     *
     * @param declaratorNode
     * @return
     */
    private boolean isStreamExpressionDeclaratorNode(ASTVariableDeclaratorId declaratorNode) {
        if (Objects.isNull(declaratorNode.getType())
                || StringUtils.isEmpty(declaratorNode.getType().getSimpleName())) {
            return true;
        }
        String imageName = declaratorNode.getType().getSimpleName().toLowerCase();
        return Objects.equals(imageName, STREAM_CONSTANT)
                || Objects.equals(imageName, OPTIONAL_CONSTANT)
                || Objects.equals(imageName, MONO_CONSTANT)
                || Objects.equals(imageName, FLUX_CONSTANT);
    }

    protected class StreamInfo {
        private String imageName;
        private Integer streamIndex;

        public String getImageName() {
            return imageName;
        }

        public void setImageName(String imageName) {
            this.imageName = imageName;
        }

        public Integer getStreamIndex() {
            return streamIndex;
        }

        public void setStreamIndex(Integer streamIndex) {
            this.streamIndex = streamIndex;
        }
    }


}
