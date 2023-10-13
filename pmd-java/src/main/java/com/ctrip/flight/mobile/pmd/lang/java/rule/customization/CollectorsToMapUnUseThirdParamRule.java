package com.ctrip.flight.mobile.pmd.lang.java.rule.customization;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightCustomizationRule;
import net.sourceforge.pmd.lang.java.ast.ASTArgumentList;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryExpression;
import net.sourceforge.pmd.lang.java.ast.JavaNode;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author haoren
 * Create at: 2023-09-08
 */
public class CollectorsToMapUnUseThirdParamRule extends FlightCustomizationRule {

    private static final String COLLECT_IMAGE_NAME = "collect";
    private static final String COLLECT_TO_MAP_IMAGE_NAME = "Collectors.toMap";
    private static final String COLLECT_TO_CONCURRENT_MAP_IMAGE_NAME = "Collectors.toConcurrentMap";
    private static final int COLLECT_TO_MAP_THIRD_PARAM_INDEX = 3;

    public CollectorsToMapUnUseThirdParamRule() {
        super();
        addRuleChainVisit(ASTPrimaryExpression.class);
    }

    @Override
    public Object visit(ASTPrimaryExpression node, Object data) {
        if (isTestClass || isTestMethod) {
            return data;
        }

        if (isCollectorMapUseThirdParameters(node)) {
            addViolationWithPrecisePosition(data, node, COLECTORS_TO_MAP_UN_USE_THIRD_PARAMETER_VIOLATION_MSG);
        }
        return data;
    }

    private boolean isCollectorMapUseThirdParameters(ASTPrimaryExpression node) {
        for (int i = 0; i < node.getNumChildren(); i++) {
            if (isContainCollect(node.getChild(i))
                    && i < node.getNumChildren()
                    && hasUseThirdParameters(node.getChild(i + 1))) {
                return true;
            }
        }
        return false;
    }

    private boolean hasUseThirdParameters(JavaNode javaNode) {
        ASTPrimaryExpression astPrimaryExpression = javaNode.getFirstDescendantOfType(ASTPrimaryExpression.class);
        if (Objects.isNull(astPrimaryExpression)) {
            return false;
        }
        List<ASTName> astNameList = astPrimaryExpression.findDescendantsOfType(ASTName.class);
        ASTArgumentList astArgumentList = astPrimaryExpression.getFirstDescendantOfType(ASTArgumentList.class);
        if (!isContainCollectorToMap(astNameList) || Objects.isNull(astArgumentList)) {
            return false;
        }

        return astArgumentList.getNumChildren() < COLLECT_TO_MAP_THIRD_PARAM_INDEX;
    }

    private boolean isContainCollect(JavaNode javaNode) {
        return StringUtils.isNotEmpty(javaNode.getImage())
                && StringUtils.equalsIgnoreCase(javaNode.getImage(), COLLECT_IMAGE_NAME);
    }

    private boolean isContainCollectorToMap(List<ASTName> astNameList) {
        return Objects.nonNull(astNameList)
                && astNameList.stream()
                .anyMatch(astName -> StringUtils.equalsIgnoreCase(astName.getImage(), COLLECT_TO_MAP_IMAGE_NAME)
                        || StringUtils.equalsIgnoreCase(astName.getImage(), COLLECT_TO_CONCURRENT_MAP_IMAGE_NAME));
    }

    private static final String COLECTORS_TO_MAP_UN_USE_THIRD_PARAMETER_VIOLATION_MSG =
            "java.customization.CollectorsToMapUnUseThirdParamRule.violation.msg";
}
