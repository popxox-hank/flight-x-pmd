package com.ctrip.flight.mobile.pmd.lang.java.rule.customization;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightJavaRule;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryExpression;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryPrefix;
import net.sourceforge.pmd.lang.java.ast.ASTPrimarySuffix;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author haoren
 * Create at: 2023-09-13
 */
@Deprecated
public class AvoidOptionalExpressionToStreamRule extends FlightJavaRule {
    private static final String STREAM_CONSTANT = "stream";
    private static final String OPTIONAL_CONSTANT = "optional";

    @Override
    public Object visit(ASTPrimaryExpression node, Object data) {
        if (checkPrimaryPrefixContainOptional(node)
                && checkPrimarySuffixContainOptional(node)) {
            addViolationWithPrecisePosition(data, node);
        }
        return data;
    }

    private boolean checkPrimarySuffixContainOptional(ASTPrimaryExpression node) {
        List<ASTPrimarySuffix> primarySuffixList = node.findChildrenOfType(ASTPrimarySuffix.class);
        if (primarySuffixList.isEmpty()) {
            return false;
        }
        return primarySuffixList.stream()
                .filter(Objects::nonNull)
                .anyMatch(primarySuffix -> matchName(primarySuffix.getImage(), STREAM_CONSTANT));
    }

    private boolean checkPrimaryPrefixContainOptional(ASTPrimaryExpression node) {
        List<ASTPrimaryPrefix> primaryPrefixList = node.findChildrenOfType(ASTPrimaryPrefix.class);
        if (primaryPrefixList.isEmpty()) {
            return false;
        }
        List<ASTName> nameList = primaryPrefixList.stream()
                .map(primaryPrefix -> primaryPrefix.findChildrenOfType(ASTName.class))
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (nameList.isEmpty()) {
            return false;
        }
        return nameList.stream()
                .filter(Objects::nonNull)
                .anyMatch(astName -> matchName(astName.getImage(), OPTIONAL_CONSTANT));
    }

    private boolean matchName(String imageName, String needMatchName) {
        if (StringUtils.isEmpty(imageName)) {
            return false;
        }
        List<String> imageList = Arrays.asList(imageName.split("\\."));
        if (imageList.isEmpty()) {
            return false;
        }
        return imageList.stream()
                .filter(Objects::nonNull)
                .map(String::toLowerCase)
                .anyMatch(x -> Objects.equals(x, needMatchName));
    }
}
